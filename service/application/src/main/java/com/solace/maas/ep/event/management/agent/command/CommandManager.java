package com.solace.maas.ep.event.management.agent.command;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.command.mapper.CommandMapper;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.processor.CommandLogStreamingProcessor;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import com.solace.maas.ep.event.management.agent.util.MdcTaskDecorator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.solace.maas.ep.event.management.agent.constants.Command.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.ACTOR_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.TRACE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.LOG_LEVEL_ERROR;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.setCommandError;

@Slf4j
@Service
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandManager {
    private final TerraformManager terraformManager;
    private final CommandMapper commandMapper;
    private final CommandPublisher commandPublisher;
    private final MessagingServiceDelegateService messagingServiceDelegateService;
    private final EventPortalProperties eventPortalProperties;
    private final ThreadPoolTaskExecutor configPushPool;
    private final CommandLogStreamingProcessor commandLogStreamingProcessor;

    public CommandManager(TerraformManager terraformManager,
                          CommandMapper commandMapper,
                          CommandPublisher commandPublisher,
                          MessagingServiceDelegateService messagingServiceDelegateService,
                          EventPortalProperties eventPortalProperties,
                          CommandLogStreamingProcessor commandLogStreamingProcessor) {
        this.terraformManager = terraformManager;
        this.commandMapper = commandMapper;
        this.commandPublisher = commandPublisher;
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.eventPortalProperties = eventPortalProperties;
        configPushPool = new ThreadPoolTaskExecutor();
        configPushPool.setCorePoolSize(eventPortalProperties.getCommandThreadPoolMinSize());
        configPushPool.setMaxPoolSize(eventPortalProperties.getCommandThreadPoolMaxSize());
        configPushPool.setQueueCapacity(eventPortalProperties.getCommandThreadPoolQueueSize());
        configPushPool.setThreadNamePrefix("config-push-pool-");
        configPushPool.setTaskDecorator(new MdcTaskDecorator());
        configPushPool.initialize();
        this.commandLogStreamingProcessor = commandLogStreamingProcessor;
    }

    public void execute(CommandMessage request) {
        CommandRequest requestBO = commandMapper.map(request);
        CompletableFuture.runAsync(() -> configPush(requestBO), configPushPool)
                .exceptionally(e -> {
                    log.error("Error running command", e);
                    Command firstCommand = requestBO.getCommandBundles().get(0).getCommands().get(0);
                    setCommandError(firstCommand, (Exception) e);
                    finalizeAndSendResponse(requestBO);
                    return null;
                });
    }

    @SuppressWarnings("PMD")
    public void configPush(CommandRequest request) {
        Map<String, String> envVars;
        try {
            envVars = setBrokerSpecificEnvVars(request.getServiceId());
        } catch (Exception e) {
            log.error("Error getting terraform variables", e);
            Command firstCommand = request.getCommandBundles().get(0).getCommands().get(0);
            setCommandError(firstCommand, e);
            finalizeAndSendResponse(request);
            return;
        }
        List<Path> executionLogFilesToClean = new ArrayList<>();

        try {
            for (CommandBundle bundle : request.getCommandBundles()) {
                // For now everything is run serially
                for (Command command : bundle.getCommands()) {
                    Path executionLog = executeCommand(request, command, envVars);
                    if (executionLog != null) {
                        streamCommandExecutionLogToEpCore(request, command, executionLog);
                        executionLogFilesToClean.add(executionLog);
                    }
                    if (exitEarlyOnFailedCommand(bundle, command)) {
                        break;
                    }

                }
            }
            finalizeAndSendResponse(request);
        } finally {
            // Clean up activity : delete all the execution log files
            cleanup(executionLogFilesToClean);
        }

    }

    private Path executeCommand(CommandRequest request,
                                Command command,
                                Map<String, String> envVars) {
        Path executionLog = null;
        try {
            switch (command.getCommandType()) {
                case terraform:
                    executionLog = terraformManager.execute(request, command, envVars);
                    break;
                default:
                    command.setResult(CommandResult.builder()
                            .status(JobStatus.error)
                            .logs(List.of(
                                    Map.of("message", "unknown command type " + command.getCommandType(),
                                            "errorType", "UnknownCommandType",
                                            "level", LOG_LEVEL_ERROR,
                                            "timestamp", OffsetDateTime.now())))
                            .build());
                    break;
            }
        } catch (Exception e) {
            log.error("Error executing command", e);
            setCommandError(command, e);
        }

        return executionLog;
    }

    private void cleanup(List<Path> listOfExecutionLogFiles) {
        try {
            commandLogStreamingProcessor.deleteExecutionLogFiles(listOfExecutionLogFiles);
        } catch (Exception e) {
            log.error("Error while deleting execution log.", e);
        }
    }

    private void streamCommandExecutionLogToEpCore(CommandRequest request, Command command, Path executionLog) {
        try {
            commandLogStreamingProcessor.streamLogsToEP(request, command, executionLog);
        } catch (Exception e) {
            log.error("Error sending logs to ep-core for command with commandCorrelationId",
                    request.getCommandCorrelationId(), e);
        }
    }


    private static boolean exitEarlyOnFailedCommand(CommandBundle bundle, Command command) {
        return Boolean.TRUE.equals(bundle.getExitOnFailure())
                && Boolean.FALSE.equals(command.getIgnoreResult())
                && (command.getResult() == null || JobStatus.error.equals(command.getResult().getStatus()));
    }

    private void finalizeAndSendResponse(CommandRequest request) {
        request.determineStatus();
        Map<String, String> topicVars = Map.of(
                "orgId", eventPortalProperties.getOrganizationId(),
                "runtimeAgentId", eventPortalProperties.getRuntimeAgentId(),
                COMMAND_CORRELATION_ID, request.getCommandCorrelationId()
        );
        CommandMessage response = new CommandMessage(request.getServiceId(),
                request.getCommandCorrelationId(),
                request.getContext(),
                request.getStatus(),
                request.getCommandBundles());
        response.setOrgId(eventPortalProperties.getOrganizationId());
        response.setTraceId(MDC.get(TRACE_ID));
        response.setActorId(MDC.get(ACTOR_ID));
        commandPublisher.sendCommandResponse(response, topicVars);
    }


    private Map<String, String> setBrokerSpecificEnvVars(String messagingServiceId) {
        Map<String, String> envVars = new HashMap<>();
        Object client = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);
        if (client instanceof SolaceHttpSemp) {
            SolaceHttpSemp solaceClient = (SolaceHttpSemp) client;
            SempClient sempClient = solaceClient.getSempClient();
            envVars.put("TF_VAR_username", sempClient.getUsername());
            envVars.put("TF_VAR_password", sempClient.getPassword());
            envVars.put("TF_VAR_url", sempClient.getConnectionUrl());
        }
        return envVars;
    }


}

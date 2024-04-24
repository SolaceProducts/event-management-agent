package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.common.messages.CommandLogMessage;
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
import com.solace.maas.ep.event.management.agent.publisher.CommandLogsPublisher;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import com.solace.maas.ep.event.management.agent.util.MdcTaskDecorator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
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
    private final CommandLogsPublisher commandLogsPublisher;
    private final MessagingServiceDelegateService messagingServiceDelegateService;
    private final EventPortalProperties eventPortalProperties;
    private final ThreadPoolTaskExecutor configPushPool;
    private final ObjectMapper objectMapper;

    public CommandManager(TerraformManager terraformManager,
                          CommandMapper commandMapper,
                          CommandPublisher commandPublisher,
                          MessagingServiceDelegateService messagingServiceDelegateService,
                          EventPortalProperties eventPortalProperties,
                          CommandLogsPublisher commandLogsPublisher,
                          ObjectMapper objectMapper) {
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
        this.commandLogsPublisher = commandLogsPublisher;
        configPushPool.initialize();
        this.objectMapper = objectMapper;
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

        for (CommandBundle bundle : request.getCommandBundles()) {
            // For now everything is run serially
            for (Command command : bundle.getCommands()) {
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

                if (exitEarlyOnFailedCommand(bundle, command)) {
                    readAllExecutionLogs(executionLog).forEach(
                            log -> sendLogToEpCore(
                                    log,
                                    request.getCommandCorrelationId(),
                                    request.getServiceId()
                            )
                    );
                    break;
                }

            }
        }

        finalizeAndSendResponse(request);
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

    private List<String> readAllExecutionLogs(Path path) {
        if (path == null) {
            return List.of();
        }
        try {
            return Files.readAllLines(path);
        } catch (Exception e) {
            log.error("Error reading execution log from path {}", path);
            throw new IllegalArgumentException(e);
        }
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

    private void sendLogToEpCore(String message,
                                 String commandCorrelationId,
                                 String messagingServiceId) {
        try {
            Map<String, String> topicDetails = new HashMap<>();
            String runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
            String orgId = eventPortalProperties.getOrganizationId();

            Map<String, Object> log = objectMapper.readValue(message, Map.class);
            CommandLogMessage logDataMessage = new CommandLogMessage(
                    orgId,
                    commandCorrelationId,
                    MDC.get(TRACE_ID),
                    MDC.get(ACTOR_ID),
                    log.get("@level").toString(),
                    message,
                    Instant.now().toEpochMilli(),
                    runtimeAgentId
            );
            topicDetails.put("orgId", eventPortalProperties.getOrganizationId());
            topicDetails.put("runtimeAgentId", eventPortalProperties.getRuntimeAgentId());
            topicDetails.put("messagingServiceId", messagingServiceId);
            topicDetails.put(COMMAND_CORRELATION_ID, commandCorrelationId);
            commandLogsPublisher.sendCommandLogData(logDataMessage, topicDetails);
        } catch (Exception e) {
            log.error("Error sending logs to ep-core", e);
            throw new IllegalArgumentException(e);
        }

    }
}

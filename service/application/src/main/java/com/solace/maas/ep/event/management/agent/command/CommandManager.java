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
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.solace.maas.ep.common.metrics.MetricConstants.ENTITY_TYPE_TAG;
import static com.solace.maas.ep.common.metrics.MetricConstants.MAAS_EMA_EVENT_CYCLE_TIME;
import static com.solace.maas.ep.common.metrics.MetricConstants.MAAS_EMA_EVENT_SENT;
import static com.solace.maas.ep.common.metrics.MetricConstants.ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.MetricConstants.STATUS_TAG;
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
    private final Optional<CommandLogStreamingProcessor> commandLogStreamingProcessorOpt;
    private final MeterRegistry meterRegistry;

    public CommandManager(TerraformManager terraformManager,
                          CommandMapper commandMapper,
                          CommandPublisher commandPublisher,
                          MessagingServiceDelegateService messagingServiceDelegateService,
                          EventPortalProperties eventPortalProperties,
                          Optional<CommandLogStreamingProcessor> commandLogStreamingProcessorOpt,
                          MeterRegistry meterRegistry) {
        this.terraformManager = terraformManager;
        this.commandMapper = commandMapper;
        this.commandPublisher = commandPublisher;
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.eventPortalProperties = eventPortalProperties;
        this.meterRegistry = meterRegistry;
        configPushPool = new ThreadPoolTaskExecutor();
        configPushPool.setCorePoolSize(eventPortalProperties.getCommandThreadPoolMinSize());
        configPushPool.setMaxPoolSize(eventPortalProperties.getCommandThreadPoolMaxSize());
        configPushPool.setQueueCapacity(eventPortalProperties.getCommandThreadPoolQueueSize());
        configPushPool.setThreadNamePrefix("config-push-pool-");
        configPushPool.setTaskDecorator(new MdcTaskDecorator());
        configPushPool.initialize();
        this.commandLogStreamingProcessorOpt = commandLogStreamingProcessorOpt;
    }

    public void execute(CommandMessage request) {
        CommandRequest requestBO = commandMapper.map(request);
        requestBO.setCreatedTime(Instant.now());
        CompletableFuture.runAsync(() -> configPush(requestBO), configPushPool)
                .exceptionally(e -> {
                    log.error("Error running command", e);
                    handleError((Exception) e, requestBO);
                    return null;
                });
    }

    public void handleError(Exception e, CommandMessage message) {
        handleError(e, commandMapper.map(message));
    }

    private void handleError(Exception e, CommandRequest requestBO) {
        Command firstCommand = requestBO.getCommandBundles().get(0).getCommands().get(0);
        setCommandError(firstCommand, e);
        finalizeAndSendResponse(requestBO);
    }

    @SuppressWarnings("PMD")
    public void configPush(CommandRequest request) {
        Map<String, String> envVars;
        try {
            envVars = setBrokerSpecificEnvVars(request.getServiceId());
        } catch (Exception e) {
            log.error("Error getting terraform variables", e);
            handleError(e, request);
            return;
        }
        List<Path> executionLogFilesToClean = new ArrayList<>();

        try {
            for (CommandBundle bundle : request.getCommandBundles()) {
                // For now everything is run serially
                for (Command command : bundle.getCommands()) {
                    Path executionLog = executeCommand(request, command, envVars);
                    if (executionLog != null) {
                        if (commandLogStreamingProcessorOpt.isPresent()) {
                            streamCommandExecutionLogToEpCore(request, command, executionLog);
                        }

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
            deleteExecutionLogFiles(listOfExecutionLogFiles);
        } catch (Exception e) {
            log.error("Error while deleting execution log.", e);
        }
    }

    public void deleteExecutionLogFiles(List<Path> listOfExecutionLogFiles) {
        boolean allFilesDeleted = listOfExecutionLogFiles
                .stream()
                .allMatch(this::deleteExecutionLogFile);
        if (!allFilesDeleted) {
            throw new IllegalArgumentException("Some of the execution log files were not deleted. Please check the logs");
        }
    }

    private boolean deleteExecutionLogFile(Path path) {
        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            log.warn("Error while deleting execution log at {}", path, e);
            return false;
        }
        return true;
    }

    public void streamCommandExecutionLogToEpCore(CommandRequest request, Command command, Path executionLog) {
        if (commandLogStreamingProcessorOpt.isEmpty()) {
            throw new UnsupportedOperationException("Streaming logs to ep is not supported for this event management agent type");
        }
        try {
            commandLogStreamingProcessorOpt.get().streamLogsToEP(request, command, executionLog);
        } catch (Exception e) {
            log.error("Error sending logs to ep-core for command with commandCorrelationId {}",
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
        meterRegistry.counter(MAAS_EMA_EVENT_SENT, ENTITY_TYPE_TAG, response.getType(),
                ORG_ID_TAG, response.getOrgId(), STATUS_TAG, response.getStatus().name()).increment();
        Timer jobCycleTime = Timer
                .builder(MAAS_EMA_EVENT_CYCLE_TIME)
                .tag(ORG_ID_TAG, response.getOrgId())
                .tag(STATUS_TAG, request.getStatus().name())
                .tag(ENTITY_TYPE_TAG, response.getType())
                .register(meterRegistry);
        jobCycleTime.record(request.getLifetime(ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
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

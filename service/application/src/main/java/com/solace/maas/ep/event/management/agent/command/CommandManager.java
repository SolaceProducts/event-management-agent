package com.solace.maas.ep.event.management.agent.command;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.command.mapper.CommandMapper;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProviderImpl;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.processor.CommandLogStreamingProcessor;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_CONFIG_PUSH_EVENT_CYCLE_TIME;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_CONFIG_PUSH_EVENT_SENT;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.STATUS_TAG;
import static com.solace.maas.ep.event.management.agent.constants.Command.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType.semp;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType.terraform;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants.SEMP_DELETE_OPERATION;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.ACTOR_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.TRACE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.LOG_LEVEL_ERROR;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.setCommandError;

@Slf4j
@Service
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandManager {
    public static final String ERROR_EXECUTING_COMMAND = "Error executing command";
    private final TerraformManager terraformManager;
    private final CommandMapper commandMapper;
    private final CommandPublisher commandPublisher;
    private final MessagingServiceDelegateService messagingServiceDelegateService;
    private final EventPortalProperties eventPortalProperties;
    private final Optional<CommandLogStreamingProcessor> commandLogStreamingProcessorOpt;
    private final MeterRegistry meterRegistry;
    private final SempDeleteCommandManager sempDeleteCommandManager;
    private final TerraformLogProcessingService terraformLoggingService;

    public CommandManager(TerraformManager terraformManager,
                          CommandMapper commandMapper,
                          CommandPublisher commandPublisher,
                          MessagingServiceDelegateService messagingServiceDelegateService,
                          EventPortalProperties eventPortalProperties,
                          Optional<CommandLogStreamingProcessor> commandLogStreamingProcessorOpt,
                          MeterRegistry meterRegistry,
                          final SempDeleteCommandManager sempDeleteCommandManager,
                          TerraformLogProcessingService terraformLoggingService) {
        this.terraformManager = terraformManager;
        this.commandMapper = commandMapper;
        this.commandPublisher = commandPublisher;
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.eventPortalProperties = eventPortalProperties;
        this.meterRegistry = meterRegistry;

        this.commandLogStreamingProcessorOpt = commandLogStreamingProcessorOpt;
        this.sempDeleteCommandManager = sempDeleteCommandManager;
        this.terraformLoggingService = terraformLoggingService;
    }

    public void execute(CommandMessage request) {
        CommandRequest requestBO = commandMapper.map(request);
        try {
            configPush(requestBO);
        } catch (Exception e) {
            // handleError will throw an exception if sending the response to EP fails
            // this can not be handled here and will be logged in SolacePersistenceMessageHandler
            handleError(e, requestBO);
        }
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
        SolaceHttpSemp solaceClient;
        try {
            solaceClient = messagingServiceDelegateService.getMessagingServiceClient(request.getServiceId());
            if (solaceClient == null) {
                log.error("Messaging service client not found for serviceId {}", request.getServiceId());
                throw new IllegalStateException("Messaging service client not found");
            }
            envVars = setBrokerSpecificEnvVars(solaceClient);
        } catch (Exception e) {
            log.error("Error while setting terraform variables", e);
            handleError(e, request);
            return;
        }
        List<Path> executionLogFilesToClean = new ArrayList<>();

        try {
            // Delete the terraform state file before running the terraform commands
            // It will delete all files in the directory of this context
            try {
                terraformManager.deleteTerraformState(request);
            } catch (RuntimeException e) {
                log.error("Error deleting terraform state", e);
                attachErrorLogToFirstTerraformCommand(e, request);
                finalizeAndSendResponse(request);
                return;
            }
            for (CommandBundle bundle : request.getCommandBundles()) {
                boolean exitEarlyOnFailedCommand = bundle.getExitOnFailure();
                // For now everything is run serially
                for (Command command : bundle.getCommands()) {
                    if (command.getCommandType() == semp) {
                        executeSempCommand(command, solaceClient);

                    } else if (command.getCommandType() == terraform) {
                        Path executionLog = executeTerraformCommand(request, command, envVars);
                        if (executionLog != null) {
                            if (commandLogStreamingProcessorOpt.isPresent()) {
                                streamCommandExecutionLogToEpCore(request, command, executionLog);
                            }
                            executionLogFilesToClean.add(executionLog);
                        }
                    } else {
                        handleUnknownCommandType(command);
                    }
                    if (exitEarlyOnFailedCommand(exitEarlyOnFailedCommand, command)) {
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

    // Attaches the error log to the first terraform command in the request to be sent back to EP although the error
    // occurred during the deletion of the terraform state file and not during the terraform command execution
    private void attachErrorLogToFirstTerraformCommand(RuntimeException ex, CommandRequest request){
        for (CommandBundle bundle : request.getCommandBundles()) {
            for (Command command : bundle.getCommands()) {
                if (command.getCommandType() == terraform) {
                    CommandResult errorResult = terraformLoggingService.buildTfStateFileDeletionFailureResult(ex);
                    command.setResult(errorResult);
                    return;
                }
            }
        }
        log.error("No terraform command found to attach error log to");
    }

    private Path executeTerraformCommand(CommandRequest request, Command command, Map<String, String> envVars) {
        Path executionLog = null;
        try {
            Validate.isTrue(command.getCommandType().equals(terraform), "Command type must be terraform");
            return terraformManager.execute(request, command, envVars);
        } catch (Exception e) {
            log.error(ERROR_EXECUTING_COMMAND, e);
            setCommandError(command, e);

        }
        return executionLog;
    }

    private void executeSempCommand(Command command, SolaceHttpSemp solaceClient) {
        try {
            Validate.isTrue(command.getCommandType().equals(semp), "Command type must be semp");
            // only delete operation is supported for now and only delete operations are sent from EP
            Validate.isTrue(command.getCommand().equals(SEMP_DELETE_OPERATION), "Command operation must be delete");
            sempDeleteCommandManager.execute(command, new SempApiProviderImpl(solaceClient));
        } catch (Exception e) {
            log.error(ERROR_EXECUTING_COMMAND, e);
            setCommandError(command, e);
        }
    }

    private void handleUnknownCommandType(Command command) {
        command.setResult(CommandResult.builder()
                .status(JobStatus.error)
                .logs(List.of(
                        Map.of("message", "unknown command type " + command.getCommandType(),
                                "errorType", "UnknownCommandType",
                                "level", LOG_LEVEL_ERROR,
                                "timestamp", OffsetDateTime.now())))
                .build());

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


    private static boolean exitEarlyOnFailedCommand(boolean existEarlyOnFailedCommand, Command command) {
        return Boolean.TRUE.equals(existEarlyOnFailedCommand)
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
        meterRegistry.counter(MAAS_EMA_CONFIG_PUSH_EVENT_SENT, ORG_ID_TAG, response.getOrgId(),
                STATUS_TAG, response.getStatus().name()).increment();
        Timer jobCycleTime = Timer
                .builder(MAAS_EMA_CONFIG_PUSH_EVENT_CYCLE_TIME)
                .tag(ORG_ID_TAG, response.getOrgId())
                .tag(STATUS_TAG, request.getStatus().name())
                .register(meterRegistry);
        jobCycleTime.record(request.getLifetime(ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
    }


    private Map<String, String> setBrokerSpecificEnvVars(SolaceHttpSemp solaceClient) {
        Map<String, String> envVars = new HashMap<>();
        SempClient sempClient = solaceClient.getSempClient();
        envVars.put("TF_VAR_username", sempClient.getUsername());
        envVars.put("TF_VAR_password", sempClient.getPassword());
        envVars.put("TF_VAR_url", sempClient.getConnectionUrl());
        return envVars;
    }


}

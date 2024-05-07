package com.solace.maas.ep.event.management.agent.processor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.common.messages.CommandLogMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import com.solace.maas.ep.event.management.agent.publisher.CommandLogsPublisher;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.solace.maas.ep.event.management.agent.constants.Command.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.ACTOR_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.TRACE_ID;

@Component
@Slf4j
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandLogStreamingProcessor {

    public static final String ANY = "*";
    private final CommandLogsPublisher commandLogsPublisher;
    private final EventPortalProperties eventPortalProperties;
    private final ObjectMapper objectMapper;

    public CommandLogStreamingProcessor(CommandLogsPublisher commandLogsPublisher,
                                        EventPortalProperties eventPortalProperties,
                                        ObjectMapper objectMapper) {
        this.commandLogsPublisher = commandLogsPublisher;
        this.eventPortalProperties = eventPortalProperties;
        this.objectMapper = objectMapper;
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


    public void streamLogsToEP(CommandRequest request, Command executedCommand, Path commandExecutionLog) {

        if (executedCommand.getIgnoreResult()) {
            log.debug("Skipping log streaming to ep for command {} with commandCorrelationId {} as ignoreResult is set to true",
                    executedCommand.getCommand(), request.getCommandCorrelationId());
            return;
        }

        if (commandExecutionLog == null) {
            throw new IllegalArgumentException(
                    String.format(
                            "Execution log was not found for command %s with commandCorrelationId %s", executedCommand.getCommand(),
                            request.getCommandCorrelationId()
                    )
            );
        }
        LogStreamingConfiguration config = LogStreamingConfiguration.CONFIG_BY_JOB_STATUS.get(
                executedCommand.getResult().getStatus()
        );
        readAllExecutionLogs(commandExecutionLog)
                .stream()
                .filter(msg -> {
                    Map<String, Object> mapRepresentationOfLog = extractMapFromJsonLogMsgStr(msg);
                    String logLevel = String.valueOf(mapRepresentationOfLog.get(TerraformLogProcessingService.KEY_LOG_LEVEL));
                    String type = String.valueOf(mapRepresentationOfLog.get(TerraformLogProcessingService.KEY_TYPE));
                    if (!config.getTargetLogLevels().contains(logLevel) && !config.getTargetLogLevels().contains(ANY)) {
                        return false;
                    }
                    return config.getTargetLogTypes().contains(type) || config.getTargetLogTypes().contains(ANY);

                })
                .map(strLog -> toCommandLogMessage(
                        eventPortalProperties.getOrganizationId(),
                        strLog,
                        request.getCommandCorrelationId(),
                        eventPortalProperties.getRuntimeAgentId()

                ))
                .forEach(

                        log -> sendLogToEpCore(
                                log,
                                request.getCommandCorrelationId(),
                                request.getServiceId()
                        )
                );
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

    private void sendLogToEpCore(CommandLogMessage logDataMessage,
                                 String commandCorrelationId,
                                 String messagingServiceId) {
        try {
            Map<String, String> topicDetails = new HashMap<>();
            topicDetails.put("orgId", eventPortalProperties.getOrganizationId());
            topicDetails.put("runtimeAgentId", eventPortalProperties.getRuntimeAgentId());
            topicDetails.put("messagingServiceId", messagingServiceId);
            topicDetails.put(COMMAND_CORRELATION_ID, commandCorrelationId);
            commandLogsPublisher.sendCommandLogData(logDataMessage, topicDetails);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    private CommandLogMessage toCommandLogMessage(String orgId,
                                                  String logMessage,
                                                  String commandCorrelationId,
                                                  String runtimeAgentId) {

        Map<String, Object> mapRepresentationOfLog = extractMapFromJsonLogMsgStr(logMessage);
        return new CommandLogMessage(
                orgId,
                commandCorrelationId,
                MDC.get(TRACE_ID),
                MDC.get(ACTOR_ID),
                String.valueOf(mapRepresentationOfLog.get(TerraformLogProcessingService.KEY_LOG_LEVEL)),
                logMessage,
                Instant.now().toEpochMilli(),
                runtimeAgentId
        );
    }


    private Map<String, Object> extractMapFromJsonLogMsgStr(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private enum LogStreamingConfiguration {
        ERROR_CASE(JobStatus.error, Set.of(ANY), Set.of(ANY)),
        SUCCESS_CASE(JobStatus.success, Set.of("info"), Set.of("change_summary"));

        private static final Map<JobStatus, LogStreamingConfiguration> CONFIG_BY_JOB_STATUS;

        static {
            CONFIG_BY_JOB_STATUS = Arrays.stream(LogStreamingConfiguration.values()).collect(
                    Collectors.collectingAndThen(
                            Collectors.toMap(LogStreamingConfiguration::getTargetJobStatus, Function.identity()),
                            Map::copyOf));
        }

        private Set<String> targetLogLevels;
        private Set<String> targetLogTypes;
        private JobStatus targetJobStatus;

        LogStreamingConfiguration(JobStatus jobStatus, Set<String> logLevels, Set<String> logTypes) {
            this.targetLogLevels = logLevels;
            this.targetLogTypes = logTypes;
            this.targetJobStatus = jobStatus;
        }

        public JobStatus getTargetJobStatus() {
            return targetJobStatus;
        }

        public Set<String> getTargetLogLevels() {
            return targetLogLevels;
        }

        public Set<String> getTargetLogTypes() {
            return targetLogTypes;
        }

        public LogStreamingConfiguration getConfigurationByStatus(JobStatus status) {
            if (!CONFIG_BY_JOB_STATUS.containsKey(status)) {
                throw new IllegalArgumentException("Unknown job status " + status);
            }
            return CONFIG_BY_JOB_STATUS.get(status);
        }
    }
}

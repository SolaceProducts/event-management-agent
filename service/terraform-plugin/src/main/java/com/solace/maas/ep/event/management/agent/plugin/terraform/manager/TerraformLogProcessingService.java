package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.FailureSeverity;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.command.model.PreFlightCheckType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TerraformLogProcessingService {
    public static final String KEY_LOG_LEVEL = "@level";
    public static final String VALUE_LOG_LEVEL_ERROR = "error";
    public static final String KEY_TYPE = "type";
    public static final String VALUE_TYPE_APPLY_COMPLETE = "apply_complete";
    public static final String VALUE_TYPE_APPLY_ERRORED = "apply_errored";
    public static final String KEY_MESSAGE = "@message";
    public static final String KEY_LEVEL = "@level";
    public static final String KEY_TIMESTAMP = "@timestamp";
    public static final String KEY_DIAGNOSTIC_DETAIL = "diagnosticDetail";
    public static final String KEY_DIAGNOSTIC = "diagnostic";
    private final ObjectMapper objectMapper;

    public TerraformLogProcessingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CommandResult buildTfStateFileDeletionFailureResult(RuntimeException rootCause) {

        Map<String, Object> errorLog = Map.of(
                "address", "N/A",
                "message", "Failed removing Terraform state: " + rootCause.getMessage(),
                "level", "ERROR",
                "timestamp", OffsetDateTime.now());

        List<Map<String, Object>> logs = List.of(errorLog);

        return CommandResult.builder()
                .logs(logs)
                .status(JobStatus.error)
                .build();
    }

    public CommandResult buildTfCommandResult(List<String> logsInJsonFormat) {

        if (CollectionUtils.isEmpty(logsInJsonFormat)) {
            throw new IllegalArgumentException("No terraform logs were collected. Unable to process response.");
        }

        // Copy the list to avoid modifying the original and to avoid concurrent modification exceptions
        List<String> jsonLogsCopy = new ArrayList<>(logsInJsonFormat);
        List<Map<String, Object>> successLogs = jsonLogsCopy.stream()
                .map(this::parseTfOutput)
                .filter(this::isApplyCompleteLog)
                .map(this::simplifyApplyCompleteLog)
                .toList();

        List<Map<String, Object>> errorLogs = jsonLogsCopy.stream()
                .map(this::parseTfOutput)
                .filter(this::isErrorLog)
                .map(this::simplifyApplyErroredLog)
                .toList();
        JobStatus status = CollectionUtils.isEmpty(errorLogs) ? JobStatus.success : JobStatus.error;

        return CommandResult.builder()
                .logs(ListUtils.union(successLogs, errorLogs))
                .status(status)
                .build();
    }

    public CommandResult buildPreFlightCheckResult(List<String> logsInJsonFormat, Command command) {
        if (CollectionUtils.isEmpty(logsInJsonFormat)) {
            throw new IllegalArgumentException("No terraform logs were collected. Unable to process response.");
        }

        //Parse logs and check for 404/not found errors
        List<Map<String, Object>> passedLogs = logsInJsonFormat.stream()
                .map(this::parseTfOutput)
                .toList();

        //For client profile checks, 404 means the profile does not exist
        boolean isResourceNotFound = passedLogs.stream()
                .anyMatch(log -> log.containsKey("error") &&
                        String.valueOf(log.get("error")).equals("resource not found"));

        // TODO:
        JobStatus status = isResourceNotFound ? JobStatus.warning : JobStatus.error;

        // Create appropriate log message for missing client profile
        List<Map<String, Object>> logs;
        if (isResourceNotFound && command.getPreFlightCheckType() == PreFlightCheckType.CLIENT_PROFILE_EXISTENCE) {
            //Extract client profile name from command parameters
            Map<String, Object> data = (Map<String, Object>) command.getParameters().get("data");
            String clientProfileName = (String) data.get("clientProfileName");

            logs = List.of(Map.of(
                    "address", "N/A", // TODO: not sure if this is correct, will come back on this
                    "message", "Required client profile '" + clientProfileName + "' does not exist",
                    "level", "WARN",
                    "timestamp", OffsetDateTime.now()
            ));
        } else {
            logs = passedLogs.stream()
                    .map(log -> {
                        // Simplify and normalize log format
                        return Map.of(
                                "address", log.containsKey("resource") ? String.valueOf(log.get("resource")) : "N/A",
                                "message", log.containsKey("message") ? String.valueOf(log.get("message")) :
                                        (log.containsKey("error") ? String.valueOf(log.get("error")) : "Unknown status"),
                                "level", command.getFailureSeverity() == FailureSeverity.WARNING ? "WARN" : "ERROR",
                                "timestamp", log.containsKey("@timestamp") ? log.get("@timestamp") : OffsetDateTime.now()
                        );
                    })
                    .collect(Collectors.toList());
        }
        return CommandResult.builder()
                .logs(logs)
                .status(status)
                .build();
    }


    private Map<String, Object> parseTfOutput(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JSON: " + json, e);
        }
    }

    private String extractValueFromLog(Map<String, Object> map, String key) {
        return String.valueOf(map.get(key));
    }

    private Map<String, Object> simplifyApplyCompleteLog(Map<String, Object> expandedLogMessage) {
        if (!isApplyCompleteLog(expandedLogMessage)) {
            throw new UnsupportedOperationException(
                    String.format("This method only handles logs of type %s", VALUE_TYPE_APPLY_COMPLETE)
            );
        }

        return Map.of(
                "address", extractResourceAddressFromHook(expandedLogMessage.get("hook")),
                "message", expandedLogMessage.get(KEY_MESSAGE),
                "level", expandedLogMessage.get(KEY_LEVEL).toString().toUpperCase(),
                "timestamp", expandedLogMessage.get(KEY_TIMESTAMP)

        );

    }

    private Map<String, Object> simplifyApplyErroredLog(Map<String, Object> expandedLogMessage) {
        if (!isErrorLog(expandedLogMessage)) {
            throw new UnsupportedOperationException(
                    String.format("This method only handles logs of type %s", VALUE_TYPE_APPLY_ERRORED)
            );
        }
        return Map.of(
                "address", extractResourceAddressFromDiagnostic(expandedLogMessage.get(KEY_DIAGNOSTIC)),
                KEY_DIAGNOSTIC_DETAIL, extractDiagnosticDetailMessage(expandedLogMessage.get(KEY_DIAGNOSTIC)),
                "message", expandedLogMessage.get(KEY_MESSAGE),
                "level", expandedLogMessage.get(KEY_LEVEL).toString().toUpperCase(),
                "timestamp", expandedLogMessage.get(KEY_TIMESTAMP)
        );
    }

    private String extractDiagnosticDetailMessage(Object diagnostic) {
        if (diagnostic == null) {
            return "";
        }
        if (diagnostic instanceof Map diagnosticMap) {
            return String.valueOf(diagnosticMap.get("detail"));
        }

        throw new IllegalArgumentException("Unsupported terraform log object type encountered");
    }

    private String extractResourceAddressFromDiagnostic(Object diagnostic) {
        if (diagnostic == null) {
            return "";
        }
        if (!(diagnostic instanceof Map)) {
            throw new IllegalArgumentException("Unsupported object type encountered");
        }
        return String.valueOf(((Map<String, Object>) diagnostic).get("address"));

    }

    private String extractResourceAddressFromHook(Object hook) {
        if (hook == null) {
            return "";
        }
        if (!(hook instanceof Map)) {
            throw new IllegalArgumentException("Unsupported object type encountered");
        }
        Map<String, Object> resource = (Map<String, Object>) (((Map<String, Object>) hook).get("resource"));
        return String.valueOf(resource.get("addr"));

    }

    private boolean isApplyCompleteLog(Map<String, Object> expandedLogMessage) {
        return StringUtils.equals(extractValueFromLog(expandedLogMessage, KEY_TYPE), VALUE_TYPE_APPLY_COMPLETE);
    }

    private boolean isErrorLog(Map<String, Object> expandedLogMessage) {
        return StringUtils.equals(extractValueFromLog(expandedLogMessage, KEY_LOG_LEVEL), VALUE_LOG_LEVEL_ERROR);
    }
}

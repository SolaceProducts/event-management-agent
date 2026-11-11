package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String KEY_RESULT_TIMESTAMP = "timestamp";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SIMPLE_MESSAGE = "message";
    public static final String KEY_SIMPLE_LEVEL = "level";
    // EmaErrorCode enum names and messages — kept as string literals to avoid a dependency on the application module
    static final String EMA_CODE_ACL_PROFILE_LIMIT_EXCEEDED = "ACL_PROFILE_LIMIT_EXCEEDED";
    static final String EMA_CODE_INTERNAL_ERROR = "INTERNAL_ERROR";
    static final String EMA_MSG_TERRAFORM_STATE_DELETION_FAILURE =
            "The Event Management Agent encountered an internal error preparing for deployment. Contact support if the issue persists.";
    private final ObjectMapper objectMapper;

    public TerraformLogProcessingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CommandResult buildTfStateFileDeletionFailureResult(RuntimeException rootCause) {

        Map<String, Object> errorLog = new HashMap<>();
        errorLog.put(KEY_ADDRESS, "N/A");
        errorLog.put(KEY_SIMPLE_MESSAGE, "Failed removing Terraform state: " + rootCause.getMessage());
        errorLog.put(KEY_SIMPLE_LEVEL, "ERROR");
        errorLog.put(KEY_RESULT_TIMESTAMP, OffsetDateTime.now());
        errorLog.put("userMessage", EMA_MSG_TERRAFORM_STATE_DELETION_FAILURE);
        errorLog.put("emaCode", "TERRAFORM_STATE_DELETION_FAILURE");

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
                KEY_ADDRESS, extractResourceAddressFromHook(expandedLogMessage.get("hook")),
                KEY_SIMPLE_MESSAGE, expandedLogMessage.get(KEY_MESSAGE),
                KEY_SIMPLE_LEVEL, expandedLogMessage.get(KEY_LEVEL).toString().toUpperCase(),
                KEY_RESULT_TIMESTAMP, expandedLogMessage.get(KEY_TIMESTAMP)
        );

    }

    private Map<String, Object> simplifyApplyErroredLog(Map<String, Object> expandedLogMessage) {
        if (!isErrorLog(expandedLogMessage)) {
            throw new UnsupportedOperationException(
                    String.format("This method only handles logs of type %s", VALUE_TYPE_APPLY_ERRORED)
            );
        }
        Object diagnostic = expandedLogMessage.get(KEY_DIAGNOSTIC);
        String address = extractResourceAddressFromDiagnostic(diagnostic);
        String detail = extractDiagnosticDetailMessage(diagnostic);
        String summary = extractDiagnosticSummary(diagnostic);
        String emaCode = classifyTerraformDiagnostic(summary, address, detail);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put(KEY_ADDRESS, address);
        result.put(KEY_DIAGNOSTIC_DETAIL, detail);
        result.put(KEY_SIMPLE_MESSAGE, expandedLogMessage.get(KEY_MESSAGE));
        result.put(KEY_SIMPLE_LEVEL, expandedLogMessage.get(KEY_LEVEL).toString().toUpperCase());
        result.put(KEY_RESULT_TIMESTAMP, expandedLogMessage.get(KEY_TIMESTAMP));
        result.put("emaCode", emaCode);
        return result;
    }

    private String classifyTerraformDiagnostic(String summary, String address, String detail) {
        return isAclProfileLimitExceededDiagnostic(summary, address, detail)
                ? EMA_CODE_ACL_PROFILE_LIMIT_EXCEEDED
                : EMA_CODE_INTERNAL_ERROR;
    }

    /**
     * Returns true when the log map represents an ACL profile limit-exceeded error.
     * Used by TerraformManager to decide whether to downgrade the log level to WARN.
     */
    static boolean isAclProfileLimitExceeded(Map<String, Object> logMap) {
        try {
            Object diagnostic = logMap.get(KEY_DIAGNOSTIC);
            if (!(diagnostic instanceof Map<?, ?> diagnosticMap)) {
                return false;
            }
            String summary = diagnosticMap.get("summary") instanceof String s ? s : null;
            String address = diagnosticMap.get(KEY_ADDRESS) instanceof String s ? s : null;
            String detail  = diagnosticMap.get("detail")  instanceof String s ? s : null;
            return isAclProfileLimitExceededDiagnostic(summary, address, detail);
        } catch (Exception _) {
            return false;
        }
    }

    private static boolean isAclProfileLimitExceededDiagnostic(String summary, String address, String detail) {
        return "SEMP call failed".equals(summary)
                && address != null && address.startsWith("solacebroker_msg_vpn_acl_profile")
                && detail != null && detail.contains("MAX_NUM_EXCEEDED");
    }

    private String extractDiagnosticSummary(Object diagnostic) {
        if (diagnostic instanceof Map) {
            Object summary = ((Map<?, ?>) diagnostic).get("summary");
            return summary != null ? String.valueOf(summary) : null;
        }
        return null;
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

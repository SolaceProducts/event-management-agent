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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    // These string literals must match EmaErrorCode enum names in the application module exactly.
    // terraform-plugin cannot depend on application, so they are duplicated here.
    // EmaErrorCode.fromName() is null-safe and silently falls back to INTERNAL_ERROR on mismatch —
    // there is no build or test failure if these go out of sync.
    static final String EMA_CODE_ACL_PROFILE_LIMIT_EXCEEDED = "ACL_PROFILE_LIMIT_EXCEEDED";
    static final String EMA_CODE_INTERNAL_ERROR = "INTERNAL_ERROR";
    static final String EMA_CODE_TERRAFORM_INVALID_ATTRIBUTE_VALUE = "TERRAFORM_INVALID_ATTRIBUTE_VALUE";
    private static final String SUMMARY_INVALID_ATTRIBUTE_VALUE = "Invalid Attribute Value";
    private static final String SUMMARY_INVALID_ATTRIBUTE_VALUE_MATCH = "Invalid Attribute Value Match";
    private static final Pattern GOT_SUFFIX_PATTERN = Pattern.compile(",\\s*got:.*$", Pattern.DOTALL);
    private static final Pattern ATTRIBUTE_NAME_PATTERN = Pattern.compile("^(Attribute )(\\S+)( .*)$", Pattern.DOTALL);

    private static final Map<String, String> SEMP_STATUS_TO_EMA_CODE = Map.of(
            "INVALID_PARAMETER", "TERRAFORM_INVALID_PARAMETER",
            "INVALID_OPERATION", "TERRAFORM_INVALID_OPERATION",
            "PARSE_ERROR",       "TERRAFORM_PARSE_ERROR",
            "MAX_NUM_EXCEEDED",  "TERRAFORM_MAX_NUM_EXCEEDED",
            "FAIL",              "TERRAFORM_FAIL",
            "ALREADY_EXISTS",    "TERRAFORM_ALREADY_EXISTS"
    );

    // Matches: "request failed for <URL> using <METHOD>, <reason>, <STATUS>"
    // Group 1 = reason, group 2 = status
    private static final Pattern SEMP_DETAIL_PATTERN =
            Pattern.compile("^request failed for \\S+ using \\w+, (.*), ([A-Z_]+)$");
    private static final Pattern HTTP_METHOD_PREFIX_PATTERN =
            Pattern.compile("^Problem with (GET|POST|PUT|PATCH|DELETE): (.*)");
    static final String EMA_MSG_TERRAFORM_STATE_DELETION_FAILURE =
            "The Event Management Agent encountered an unexpected error. Contact support if the issue persists.";
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
        errorLog.put("emaCode", EMA_CODE_INTERNAL_ERROR);

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
        List<Map<String, Object>> parsedLogs = jsonLogsCopy.stream()
                .map(this::parseTfOutput)
                .toList();

        List<Map<String, Object>> successLogs = parsedLogs.stream()
                .filter(this::isApplyCompleteLog)
                .map(this::simplifyApplyCompleteLog)
                .toList();

        List<Map<String, Object>> errorLogs = parsedLogs.stream()
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
        ClassificationResult classification = classifyTerraformDiagnostic(summary, address, detail);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put(KEY_ADDRESS, address);
        result.put(KEY_DIAGNOSTIC_DETAIL, detail);
        result.put(KEY_SIMPLE_MESSAGE, expandedLogMessage.get(KEY_MESSAGE));
        result.put(KEY_SIMPLE_LEVEL, expandedLogMessage.get(KEY_LEVEL).toString().toUpperCase());
        result.put(KEY_RESULT_TIMESTAMP, expandedLogMessage.get(KEY_TIMESTAMP));
        result.put("emaCode", classification.emaCode());
        if (classification.userMessage() != null) {
            result.put("userMessage", classification.userMessage());
        }
        return result;
    }

    record ClassificationResult(String emaCode, String userMessage) {}

    private ClassificationResult classifyTerraformDiagnostic(String summary, String address, String detail) {
        if (isAclProfileLimitExceededDiagnostic(summary, address, detail)) {
            return new ClassificationResult(EMA_CODE_ACL_PROFILE_LIMIT_EXCEEDED, null);
        }
        if (SUMMARY_INVALID_ATTRIBUTE_VALUE.equals(summary) || SUMMARY_INVALID_ATTRIBUTE_VALUE_MATCH.equals(summary)) {
            return classifyInvalidAttributeValue(detail);
        }
        if ("SEMP call failed".equals(summary) && detail != null) {
            return classifySempCallFailed(detail);
        }
        return new ClassificationResult(EMA_CODE_INTERNAL_ERROR, null);
    }

    private ClassificationResult classifyInvalidAttributeValue(String detail) {
        if (detail == null) {
            return new ClassificationResult(EMA_CODE_TERRAFORM_INVALID_ATTRIBUTE_VALUE, null);
        }
        String stripped = GOT_SUFFIX_PATTERN.matcher(detail).replaceFirst("");
        if (stripped.equals(detail)) {
            return new ClassificationResult(EMA_CODE_TERRAFORM_INVALID_ATTRIBUTE_VALUE, null);
        }
        Matcher m = ATTRIBUTE_NAME_PATTERN.matcher(stripped);
        String userMessage = m.matches() ? m.group(1) + snakeToCamel(m.group(2)) + m.group(3) : stripped;
        return new ClassificationResult(EMA_CODE_TERRAFORM_INVALID_ATTRIBUTE_VALUE, userMessage);
    }

    private ClassificationResult classifySempCallFailed(String detail) {
        Matcher matcher = SEMP_DETAIL_PATTERN.matcher(detail);
        if (!matcher.matches()) {
            return new ClassificationResult(EMA_CODE_INTERNAL_ERROR, null);
        }
        String reason = matcher.group(1);
        String status = matcher.group(2);
        String emaCode = SEMP_STATUS_TO_EMA_CODE.getOrDefault(status, EMA_CODE_INTERNAL_ERROR);
        if (EMA_CODE_INTERNAL_ERROR.equals(emaCode)) {
            return new ClassificationResult(emaCode, null);
        }
        Matcher prefixMatcher = HTTP_METHOD_PREFIX_PATTERN.matcher(reason);
        String userMessage = prefixMatcher.matches() ? prefixMatcher.group(2) : reason;
        return new ClassificationResult(emaCode, userMessage);
    }

    /**
     * Returns true when the log map represents an ACL profile limit-exceeded error.
     * Used by TerraformManager to decide whether to downgrade the log level to WARN.
     */
    public static boolean isAclProfileLimitExceeded(Map<String, Object> logMap) {
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

    private static String snakeToCamel(String snake) {
        String[] parts = snake.split("_", -1);
        if (parts.length == 1) {
            return snake;
        }
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                sb.append(Character.toUpperCase(parts[i].charAt(0)));
                sb.append(parts[i].substring(1));
            }
        }
        return sb.toString();
    }
}

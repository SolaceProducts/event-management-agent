package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static final String KEY_DIAGNOSTIC_DETAIL = "diagnosticDetail";
    public static final String KEY_DIAGNOSTIC = "diagnostic";
    private final TerraformProperties terraformProperties;
    private final ObjectMapper objectMapper;

    public TerraformLogProcessingService(ObjectMapper objectMapper, TerraformProperties terraformProperties) {
        this.objectMapper = objectMapper;
        this.terraformProperties = terraformProperties;
    }

    public void saveLogToFile(CommandRequest request, List<String> logs) throws IOException {
        Path logPath = Paths.get(terraformProperties.getWorkingDirectoryRoot()
                + File.separator
                + request.getContext()
                + "-"
                + request.getServiceId()
                + File.separator
                + "logs"
        );

        if (Files.notExists(logPath)) {
            Files.createDirectories(logPath);
        }

        Path out = Files.createTempFile(logPath, System.currentTimeMillis() + "-"
                + request.getCorrelationId() + "-job", ".log");

        Files.write(out, logs, Charset.defaultCharset());
    }

    public CommandResult buildTfCommandResult(List<String> jsonLogs) {
        if (CollectionUtils.isEmpty(jsonLogs)) {
            throw new IllegalArgumentException("No terraform logs were collected. Unable to process response.");
        }

        List<Map<String, Object>> successLogs = jsonLogs.stream()
                .map(this::parseTfOutput)
                .filter(this::isApplyCompleteLog)
                .map(this::simplifyApplyCompleteLog)
                .toList();

        List<Map<String, Object>> errorLogs = jsonLogs.stream()
                .map(this::parseTfOutput)
                .filter(this::isErrorLog)
                .map(this::simplifyApplyErroredLog)
                .toList();
        JobStatus status = CollectionUtils.isEmpty(errorLogs) ? JobStatus.success : JobStatus.error;
        return CommandResult.builder()
                .logs(successLogs)
                .errors(errorLogs)
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
                "message", expandedLogMessage.get(KEY_MESSAGE)
        );

    }

    private Map<String, Object> simplifyApplyErroredLog(Map<String, Object> expandedLogMessage) {
        if (!isErrorLog(expandedLogMessage)) {
            throw new UnsupportedOperationException(
                    String.format("This method only handles logs of type %s", VALUE_TYPE_APPLY_ERRORED)
            );
        }
        //return expandedLogMessage;
        return Map.of(
                "address", extractResourceAddressFromDiagnostic(expandedLogMessage.get(KEY_DIAGNOSTIC)),
                KEY_DIAGNOSTIC_DETAIL, extractDiagnosticDetailMessage(expandedLogMessage.get(KEY_DIAGNOSTIC))
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

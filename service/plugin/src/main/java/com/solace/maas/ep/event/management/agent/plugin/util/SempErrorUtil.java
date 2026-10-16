package com.solace.maas.ep.event.management.agent.plugin.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

public final class SempErrorUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String UNKNOWN = "unknown";

    private SempErrorUtil() {
    }

    /**
     * Extracts the SEMP error status and code (e.g. "NOT_FOUND (code 6)") from a SEMP v2 error response body.
     * SEMP reports many distinct failures as HTTP 400, so the status is needed to tell them apart.
     * Only status and code are returned: the error description and request URI can contain entity names,
     * which are considered sensitive and must not be logged.
     */
    public static String getErrorSummary(String responseBody) {
        if (StringUtils.isBlank(responseBody)) {
            return UNKNOWN;
        }
        try {
            JsonNode error = OBJECT_MAPPER.readTree(responseBody).path("meta").path("error");
            String status = error.path("status").asText(null);
            if (StringUtils.isEmpty(status)) {
                return UNKNOWN;
            }
            return error.has("code") ? status + " (code " + error.path("code").asText() + ")" : status;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}

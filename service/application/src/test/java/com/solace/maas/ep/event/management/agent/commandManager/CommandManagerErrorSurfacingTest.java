package com.solace.maas.ep.event.management.agent.commandManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CommandManagerErrorSurfacingTest {

    private final TerraformLogProcessingService service =
            new TerraformLogProcessingService(new ObjectMapper());

    @Test
    void extractUserMessage_returnsFirstDiagnosticDetail() {
        List<Map<String, Object>> logs = List.of(
                Map.of("diagnosticDetail", "ACL Profile limit of 5002 reached., MAX_NUM_EXCEEDED",
                        "address", "solacebroker_msg_vpn_acl_profile.acl1",
                        "level", "ERROR",
                        "message", "apply error",
                        "@timestamp", "2026-10-30T00:00:00Z")
        );
        String msg = service.extractUserMessageFromErrorLogs(logs);
        assertThat(msg).isEqualTo("ACL Profile limit of 5002 reached., MAX_NUM_EXCEEDED");
    }

    @Test
    void extractUserMessage_returnsNullWhenNoLogs() {
        assertThat(service.extractUserMessageFromErrorLogs(List.of())).isNull();
        assertThat(service.extractUserMessageFromErrorLogs(null)).isNull();
    }

    @Test
    void extractUserMessage_returnsNullWhenNoDiagnosticDetail() {
        List<Map<String, Object>> logs = List.of(
                Map.of("message", "apply_complete", "level", "INFO")
        );
        assertThat(service.extractUserMessageFromErrorLogs(logs)).isNull();
    }

    @Test
    void extractUserMessage_skipsBlankDiagnosticDetail() {
        List<Map<String, Object>> logs = List.of(
                Map.of("diagnosticDetail", "   ", "level", "ERROR"),
                Map.of("diagnosticDetail", "Real error detail here", "level", "ERROR")
        );
        assertThat(service.extractUserMessageFromErrorLogs(logs))
                .isEqualTo("Real error detail here");
    }
}

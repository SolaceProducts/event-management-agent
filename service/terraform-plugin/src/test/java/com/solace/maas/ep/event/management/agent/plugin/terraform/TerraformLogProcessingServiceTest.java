package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class TerraformLogProcessingServiceTest {

    private final TerraformLogProcessingService service = new TerraformLogProcessingService(new ObjectMapper());

    @Test
    void testBuildTfStateFileDeletionFailureResult() {
        RuntimeException rootCause = new RuntimeException("rootCause");
        CommandResult result = service.buildTfStateFileDeletionFailureResult(rootCause);
        assertThat(result.getStatus()).isEqualTo(JobStatus.error);
        assertThat(result.getLogs()).hasSize(1);
        Map<String, Object> log = result.getLogs().get(0);
        assertThat(log).containsEntry("message", "Failed removing Terraform state: rootCause");
        assertThat(log).containsEntry("level", "ERROR");
        assertThat(log).containsKey("timestamp");
        assertThat(log).containsKey("userMessage");
        assertThat(log).containsEntry("emaCode", "TERRAFORM_STATE_DELETION_FAILURE");
    }

    @Nested
    class ClassifyTerraformDiagnosticTests {

        @Test
        void aclProfileLimitExceeded_allConditionsMet_returnsLimitExceededCode() {
            CommandResult result = service.buildTfCommandResult(List.of(aclProfileErrorLog()));
            assertThat(result.getStatus()).isEqualTo(JobStatus.error);
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "ACL_PROFILE_LIMIT_EXCEEDED");
        }

        @Test
        void aclProfileLimitExceeded_wrongSummary_fallsBackToInternalError() {
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("Not SEMP call failed", "solacebroker_msg_vpn_acl_profile.foo", "MAX_NUM_EXCEEDED in response")
            ));
            assertThat((Map<String, Object>) result.getLogs().get(0)).containsEntry("emaCode", "INTERNAL_ERROR");
        }

        @Test
        void aclProfileLimitExceeded_wrongAddress_fallsBackToInternalError() {
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_queue.foo", "MAX_NUM_EXCEEDED in response")
            ));
            assertThat((Map<String, Object>) result.getLogs().get(0)).containsEntry("emaCode", "INTERNAL_ERROR");
        }

        @Test
        void aclProfileLimitExceeded_detailMissingKeyword_fallsBackToInternalError() {
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_acl_profile.foo", "some other error")
            ));
            assertThat((Map<String, Object>) result.getLogs().get(0)).containsEntry("emaCode", "INTERNAL_ERROR");
        }

        private String aclProfileErrorLog() {
            return errorLog("SEMP call failed", "solacebroker_msg_vpn_acl_profile.test-acl",
                    "SEMP response: 400 Bad Request — MAX_NUM_EXCEEDED for ACL profiles");
        }

        private String errorLog(String summary, String address, String detail) {
            return String.format(
                    "{\"@level\":\"error\",\"@message\":\"Error: Provider returned error\"," +
                    "\"@timestamp\":\"2026-01-01T00:00:00.000Z\"," +
                    "\"diagnostic\":{\"severity\":\"error\",\"summary\":\"%s\",\"address\":\"%s\",\"detail\":\"%s\"}}",
                    summary, address, detail
            );
        }
    }
}

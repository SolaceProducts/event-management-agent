package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("TEST")
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
        assertThat(log).containsEntry("emaCode", "INTERNAL_ERROR");
    }

    @Test
    void buildTfCommandResult_emptyList_throwsIllegalArgument() {
        List<String> emptyList = List.of();
        assertThrows(IllegalArgumentException.class, () -> service.buildTfCommandResult(emptyList));
    }

    @Test
    void buildTfCommandResult_applyCompleteLog_returnsSuccess() {
        String applyCompleteLog =
                "{\"@level\":\"info\",\"@message\":\"Apply complete!\",\"@timestamp\":\"2026-01-01T00:00:00.000Z\"," +
                "\"type\":\"apply_complete\"," +
                "\"hook\":{\"resource\":{\"addr\":\"solacebroker_msg_vpn_queue.myqueue\",\"module\":\"\",\"resource\":\"solacebroker_msg_vpn_queue.myqueue\",\"implied_provider\":\"solacebroker\",\"resource_type\":\"solacebroker_msg_vpn_queue\",\"resource_name\":\"myqueue\",\"resource_key\":null}," +
                "\"action\":\"create\",\"id_key\":\"queue_name\",\"id_value\":\"myqueue\",\"elapsed_seconds\":0}}";

        CommandResult result = service.buildTfCommandResult(List.of(applyCompleteLog));

        assertThat(result.getStatus()).isEqualTo(JobStatus.success);
        assertThat(result.getLogs()).hasSize(1);
        Map<String, Object> log = result.getLogs().get(0);
        assertThat(log).containsEntry("address", "solacebroker_msg_vpn_queue.myqueue");
        assertThat(log).containsEntry("level", "INFO");
        assertThat(log).containsKey("timestamp");
    }

    @Test
    void buildTfCommandResult_mixedLogs_separatesSuccessAndErrors() {
        String applyCompleteLog =
                "{\"@level\":\"info\",\"@message\":\"Apply complete!\",\"@timestamp\":\"2026-01-01T00:00:00.000Z\"," +
                "\"type\":\"apply_complete\"," +
                "\"hook\":{\"resource\":{\"addr\":\"solacebroker_msg_vpn_queue.myqueue\",\"module\":\"\",\"resource\":\"solacebroker_msg_vpn_queue.myqueue\",\"implied_provider\":\"solacebroker\",\"resource_type\":\"solacebroker_msg_vpn_queue\",\"resource_name\":\"myqueue\",\"resource_key\":null}," +
                "\"action\":\"create\",\"id_key\":\"queue_name\",\"id_value\":\"myqueue\",\"elapsed_seconds\":0}}";
        String errorLog =
                "{\"@level\":\"error\",\"@message\":\"Error: Provider returned error\"," +
                "\"@timestamp\":\"2026-01-01T00:00:00.000Z\"," +
                "\"diagnostic\":{\"severity\":\"error\",\"summary\":\"SEMP call failed\",\"address\":\"solacebroker_msg_vpn_queue.foo\",\"detail\":\"some error\"}}";

        CommandResult result = service.buildTfCommandResult(List.of(applyCompleteLog, errorLog));

        assertThat(result.getStatus()).isEqualTo(JobStatus.error);
        assertThat(result.getLogs()).hasSize(2);
    }

    @Nested
    class IsAclProfileLimitExceededTests {

        @Test
        void noDiagnosticKey_returnsFalse() {
            assertThat(TerraformLogProcessingService.isAclProfileLimitExceeded(Map.of("@level", "error"))).isFalse();
        }

        @Test
        void diagnosticIsNotMap_returnsFalse() {
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("diagnostic", "not-a-map");
            assertThat(TerraformLogProcessingService.isAclProfileLimitExceeded(logMap)).isFalse();
        }

        @Test
        void exceptionDuringAccess_returnsFalse() {
            Map<String, Object> logMap = new HashMap<>() {
                @Override
                public Object get(Object key) {
                    if ("diagnostic".equals(key)) throw new RuntimeException("simulated");
                    return super.get(key);
                }
            };
            assertThat(TerraformLogProcessingService.isAclProfileLimitExceeded(logMap)).isFalse();
        }

        @Test
        void allConditionsMet_returnsTrue() {
            Map<String, Object> diagnostic = Map.of(
                    "summary", "SEMP call failed",
                    "address", "solacebroker_msg_vpn_acl_profile.test",
                    "detail", "400 Bad Request — MAX_NUM_EXCEEDED"
            );
            Map<String, Object> logMap = Map.of("diagnostic", diagnostic);
            assertThat(TerraformLogProcessingService.isAclProfileLimitExceeded(logMap)).isTrue();
        }
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

        // ── Broker SEMP error classification ────────────────────────────────

        @Test
        void sempCallFailed_invalidParameter_classifiesCorrectlyAndExtractsReason() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/queues/q1 " +
                    "using PUT, Unknown attribute 'respectDmqEligibleEnabled', INVALID_PARAMETER";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_queue.q1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "TERRAFORM_INVALID_PARAMETER");
            assertThat(log).containsEntry("userMessage", "Unknown attribute 'respectDmqEligibleEnabled'");
        }

        @Test
        void sempCallFailed_invalidParameter_unexpectedParameterWording_classifiesCorrectly() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/queues/q1 " +
                    "using PUT, Unexpected Parameter 'respectDmqEligibleEnabled', INVALID_PARAMETER";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_queue.q1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "TERRAFORM_INVALID_PARAMETER");
            assertThat(log).containsEntry("userMessage", "Unexpected Parameter 'respectDmqEligibleEnabled'");
        }

        @Test
        void sempCallFailed_invalidParameter_problemWithWording_classifiesCorrectly() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/queues/q1 " +
                    "using PUT, Problem with owner: The name should have only the printable ASCII characters different from wildcards(? and *)., INVALID_PARAMETER";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_queue.q1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "TERRAFORM_INVALID_PARAMETER");
            assertThat(log).containsEntry("userMessage",
                    "Problem with owner: The name should have only the printable ASCII characters different from wildcards(? and *).");
        }

        @Test
        void sempCallFailed_invalidOperation_classifiesCorrectlyAndExtractsReason() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/rdps/rdp1 " +
                    "using PUT, Cannot modify read-only attribute 'headerName', INVALID_OPERATION";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_rest_delivery_point.rdp1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "TERRAFORM_INVALID_OPERATION");
            assertThat(log).containsEntry("userMessage", "Cannot modify read-only attribute 'headerName'");
        }

        @Test
        void sempCallFailed_fail_classifiesCorrectlyAndExtractsReason() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/clients/c1 " +
                    "using PUT, Problem with authenticationClientCertContent: Private key not found or not supported., FAIL";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_client_username.c1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "TERRAFORM_FAIL");
            assertThat(log).containsEntry("userMessage",
                    "Problem with authenticationClientCertContent: Private key not found or not supported.");
        }

        @Test
        void sempCallFailed_parseError_classifiesCorrectlyAndExtractsReason() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/rdps/rdp1 " +
                    "using POST, Problem with postRequestTarget: Cannot parse POST request target: Unexpected character '/' at offset 20, PARSE_ERROR";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_rest_delivery_point.rdp1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "TERRAFORM_PARSE_ERROR");
            assertThat(log).containsEntry("userMessage",
                    "Problem with postRequestTarget: Cannot parse POST request target: Unexpected character '/' at offset 20");
        }

        @Test
        void sempCallFailed_maxNumExceeded_nonAclAddress_classifiesCorrectlyAndExtractsReason() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/queues/q1 " +
                    "using PUT, Problem with PUT: No more queues are allowed, MAX_NUM_EXCEEDED";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_queue.q1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "TERRAFORM_MAX_NUM_EXCEEDED");
            assertThat(log).containsEntry("userMessage", "Problem with PUT: No more queues are allowed");
        }

        @Test
        void sempCallFailed_maxNumExceeded_aclAddress_stillClassifiesAsAclProfileLimitExceeded() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/aclProfiles/app-123 " +
                    "using PUT, Problem with PUT: ACL Profile limit of 5002 reached., MAX_NUM_EXCEEDED";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_acl_profile.test", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "ACL_PROFILE_LIMIT_EXCEEDED");
        }

        @Test
        void sempCallFailed_unknownStatus_fallsBackToInternalError() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/queues/q1 " +
                    "using PUT, Something went wrong, SOME_UNKNOWN_STATUS";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("SEMP call failed", "solacebroker_msg_vpn_queue.q1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "INTERNAL_ERROR");
        }

        @Test
        void nonSempCallFailed_summary_fallsBackToInternalError() {
            String detail = "request failed for https://broker.example.com:943/SEMP/v2/config/msgVpns/vpn1/queues/q1 " +
                    "using PUT, Something went wrong, INVALID_PARAMETER";
            CommandResult result = service.buildTfCommandResult(List.of(
                    errorLog("Not a SEMP error", "solacebroker_msg_vpn_queue.q1", detail)
            ));
            Map<String, Object> log = result.getLogs().get(0);
            assertThat(log).containsEntry("emaCode", "INTERNAL_ERROR");
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

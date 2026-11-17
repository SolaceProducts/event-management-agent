package com.solace.maas.ep.event.management.agent.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class EmaErrorCodeTest {

    @ParameterizedTest
    @EnumSource(EmaErrorCode.class)
    void allCodesHaveRequiredFields(EmaErrorCode code) {
        assertThat(code.getServiceId()).isNotBlank();
        assertThat(code.getResult()).isNotBlank();
        assertThat(code.getCode()).isGreaterThanOrEqualTo(0);
        if (code.useEnumUserMessage()) {
            assertThat(code.getUserMessage()).isNotBlank();
        } else {
            assertThat(code.getUserMessage()).isNull();
        }
    }

    @Test
    void fromName_null_returnsNull() {
        assertThat(EmaErrorCode.fromName(null)).isNull();
    }

    @Test
    void fromName_unknownName_returnsNull() {
        assertThat(EmaErrorCode.fromName("NOT_A_REAL_CODE")).isNull();
    }

    @Test
    void fromName_validName_returnsCode() {
        assertThat(EmaErrorCode.fromName("INTERNAL_ERROR")).isEqualTo(EmaErrorCode.INTERNAL_ERROR);
    }

    @Test
    void terraformApplyError_hasExpectedUserMessage() {
        assertThat(EmaErrorCode.ACL_PROFILE_LIMIT_EXCEEDED.getUserMessage())
                .contains("ACL profile");
    }

    // These string literals are duplicated in TerraformLogProcessingService (terraform-plugin cannot depend on
    // application). This test catches drift if an enum value is renamed without updating the plugin constants.
    @ParameterizedTest
    @ValueSource(strings = {
            "ACL_PROFILE_LIMIT_EXCEEDED",
            "INTERNAL_ERROR",
            "TERRAFORM_INVALID_PARAMETER",
            "TERRAFORM_INVALID_OPERATION",
            "TERRAFORM_PARSE_ERROR",
            "TERRAFORM_MAX_NUM_EXCEEDED",
            "TERRAFORM_FAIL",
            "TERRAFORM_ALREADY_EXISTS"
    })
    void terraformPluginEmaCodeStrings_resolveToKnownEnum(String name) {
        assertThat(EmaErrorCode.fromName(name))
                .as("EmaErrorCode.fromName(\"%s\") must not be null — update TerraformLogProcessingService if the enum was renamed", name)
                .isNotNull();
    }

    @Test
    void buildInternalPayload_populatesAllFields() {
        EmaInternalPayload payload = EmaErrorCode.INTERNAL_ERROR.buildInternal("corr-123", "exit status 1");
        assertThat(payload.getCorrelationId()).isEqualTo("corr-123");
        assertThat(payload.getServiceId()).isEqualTo("ema");
        assertThat(payload.getCode()).isEqualTo(1);
        assertThat(payload.getResult()).isEqualTo("INTERNAL_ERROR");
        assertThat(payload.getErrorMessage()).isEqualTo("exit status 1");
    }
}

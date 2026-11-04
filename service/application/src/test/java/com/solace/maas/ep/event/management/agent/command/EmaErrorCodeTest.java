package com.solace.maas.ep.event.management.agent.command;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EmaErrorCodeTest {

    @Test
    void allCodesHaveServiceId() {
        for (EmaErrorCode code : EmaErrorCode.values()) {
            assertThat(code.getServiceId()).isNotBlank();
        }
    }

    @Test
    void allCodesHaveUserMessage() {
        for (EmaErrorCode code : EmaErrorCode.values()) {
            assertThat(code.getUserMessage()).isNotBlank();
        }
    }

    @Test
    void allCodesHaveNumericCode() {
        for (EmaErrorCode code : EmaErrorCode.values()) {
            assertThat(code.getCode())
                    .isNotBlank()
                    .matches("\\d+");
        }
    }

    @Test
    void allCodesHaveResult() {
        for (EmaErrorCode code : EmaErrorCode.values()) {
            assertThat(code.getResult()).isNotBlank();
        }
    }

    @Test
    void terraformApplyError_hasExpectedUserMessage() {
        assertThat(EmaErrorCode.TERRAFORM_APPLY_ERROR.getUserMessage())
                .contains("configuration");
    }

    @Test
    void buildInternalPayload_populatesAllFields() {
        EmaInternalPayload payload = EmaErrorCode.TERRAFORM_APPLY_ERROR.buildInternal("exit status 1");
        assertThat(payload.getServiceId()).isEqualTo("ema");
        assertThat(payload.getCode()).isEqualTo("909");
        assertThat(payload.getResult()).isEqualTo("INTERNAL_ERROR");
        assertThat(payload.getErrorMessage()).isEqualTo("exit status 1");
    }
}

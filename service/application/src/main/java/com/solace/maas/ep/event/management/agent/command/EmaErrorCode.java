package com.solace.maas.ep.event.management.agent.command;

/**
 * EMA error code registry per the inter-microservice error protocol.
 * Each entry maps a specific error condition to a numeric code, global result, and safe user-facing message.
 * Codes are numeric-only strings owned by EMA; the serviceId+code pair is globally unique.
 * See: https://sol-jira.atlassian.net/wiki/spaces/EventInteg/pages/6467813380
 */
public enum EmaErrorCode {

    // Catch-all Terraform apply failure — superseded by specific codes as error classes are targeted
    TERRAFORM_APPLY_ERROR(
            "ema",
            "909",
            "INTERNAL_ERROR",
            "The Event Management Agent was unable to apply broker configuration. " +
            "Check the event broker's resource limits and state, then try again."
    ),
    // Catch-all direct SEMP command failure — superseded by specific codes as error classes are targeted
    SEMP_ERROR(
            "ema",
            "910",
            "INTERNAL_ERROR",
            "The Event Management Agent received an error from the event broker. " +
            "Check the broker's configuration and try again."
    ),
    MESSAGING_SERVICE_NOT_FOUND(
            "ema",
            "801",
            "INTERNAL_ERROR",
            "The Event Management Agent could not find a connection to the event broker. " +
            "Verify that the broker is registered and connected, then try again."
    ),
    TERRAFORM_STATE_DELETION_FAILURE(
            "ema",
            "802",
            "INTERNAL_ERROR",
            "The Event Management Agent encountered an internal error preparing for deployment. " +
            Msg.CONTACT_SUPPORT
    ),
    UNKNOWN_COMMAND_TYPE(
            "ema",
            "803",
            "INTERNAL_ERROR",
            "The Event Management Agent received an unrecognized command. " +
            Msg.CONTACT_SUPPORT
    ),
    INTERNAL_ERROR(
            "ema",
            "999",
            "INTERNAL_ERROR",
            "The Event Management Agent encountered an unexpected error. " +
            Msg.CONTACT_SUPPORT
    );

    private final String serviceId;
    private final String code;
    private final String result;
    private final String userMessage;

    static final class Msg {
        static final String CONTACT_SUPPORT = "Contact support if the issue persists.";
        private Msg() {}
    }

    EmaErrorCode(String serviceId, String code, String result, String userMessage) {
        this.serviceId = serviceId;
        this.code = code;
        this.result = result;
        this.userMessage = userMessage;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public EmaInternalPayload buildInternal(String rawErrorMessage) {
        return EmaInternalPayload.builder()
                .serviceId(serviceId)
                .code(code)
                .result(result)
                .errorMessage(rawErrorMessage)
                .build();
    }
}

package com.solace.maas.ep.event.management.agent.command;

/**
 * EMA error code registry per the inter-microservice error protocol.
 * Each entry maps to a safe user-facing message and a structured internal code.
 */
public enum EmaErrorCode {

    TERRAFORM_APPLY_ERROR(
            "ema",
            "TERRAFORM_APPLY_ERROR",
            "The Event Management Agent was unable to apply broker configuration. " +
            "Check the event broker's resource limits and state, then try again."
    ),
    SEMP_ERROR(
            "ema",
            "SEMP_ERROR",
            "The Event Management Agent received an error from the event broker. " +
            "Check the broker's configuration and try again."
    ),
    MESSAGING_SERVICE_NOT_FOUND(
            "ema",
            "MESSAGING_SERVICE_NOT_FOUND",
            "The Event Management Agent could not find a connection to the event broker. " +
            "Verify that the broker is registered and connected, then try again."
    ),
    TERRAFORM_STATE_DELETION_FAILURE(
            "ema",
            "TERRAFORM_STATE_DELETION_FAILURE",
            "The Event Management Agent encountered an internal error preparing for deployment. " +
            Msg.CONTACT_SUPPORT
    ),
    UNKNOWN_COMMAND_TYPE(
            "ema",
            "UNKNOWN_COMMAND_TYPE",
            "The Event Management Agent received an unrecognized command. " +
            Msg.CONTACT_SUPPORT
    ),
    INTERNAL_ERROR(
            "ema",
            "INTERNAL_ERROR",
            "The Event Management Agent encountered an unexpected error. " +
            Msg.CONTACT_SUPPORT
    );

    private final String serviceId;
    private final String code;
    private final String userMessage;

    static final class Msg {
        static final String CONTACT_SUPPORT = "Contact support if the issue persists.";
        private Msg() {}
    }

    EmaErrorCode(String serviceId, String code, String userMessage) {
        this.serviceId = serviceId;
        this.code = code;
        this.userMessage = userMessage;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getCode() {
        return code;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public EmaInternalPayload buildInternal(String rawErrorMessage) {
        return EmaInternalPayload.builder()
                .serviceId(serviceId)
                .code(code)
                .result("error")
                .errorMessage(rawErrorMessage)
                .build();
    }
}

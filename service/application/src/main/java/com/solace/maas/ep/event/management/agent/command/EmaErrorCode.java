package com.solace.maas.ep.event.management.agent.command;

/**
 * EMA error code registry per the inter-microservice error protocol.
 * Each entry maps a specific error condition to a numeric code, global result, safe user-facing message,
 * and the log level EMA should use when this error occurs.
 * Code is coarse-grained per protocol: 0 = success, 1 = failure. Consumers branch on result, not code.
 * See: https://sol-jira.atlassian.net/wiki/spaces/EventInteg/pages/6467813380
 */
public enum EmaErrorCode {

    // Catch-all Terraform apply failure — superseded by specific codes as error classes are targeted
    TERRAFORM_APPLY_ERROR(
            "ema",
            1,
            Msg.INTERNAL_ERROR,
            "The Event Management Agent was unable to apply broker configuration. " +
            "Check the event broker's resource limits and state, then try again.",
            LogLevel.ERROR
    ),
    // Catch-all direct SEMP command failure — superseded by specific codes as error classes are targeted
    SEMP_ERROR(
            "ema",
            1,
            Msg.INTERNAL_ERROR,
            "The Event Management Agent received an error from the event broker. " +
            "Check the broker's configuration and try again.",
            LogLevel.ERROR
    ),
    MESSAGING_SERVICE_NOT_FOUND(
            "ema",
            1,
            Msg.INTERNAL_ERROR,
            "The Event Management Agent could not find a connection to the event broker. " +
            "Verify that the broker is registered and connected, then try again.",
            LogLevel.ERROR
    ),
    TERRAFORM_STATE_DELETION_FAILURE(
            "ema",
            1,
            Msg.INTERNAL_ERROR,
            "The Event Management Agent encountered an internal error preparing for deployment. " +
            Msg.CONTACT_SUPPORT,
            LogLevel.ERROR
    ),
    UNKNOWN_COMMAND_TYPE(
            "ema",
            1,
            Msg.INTERNAL_ERROR,
            "The Event Management Agent received an unrecognized command. " +
            Msg.CONTACT_SUPPORT,
            LogLevel.ERROR
    ),
    ACL_PROFILE_LIMIT_EXCEEDED(
            "ema",
            1,
            "LIMIT_EXCEEDED",
            "The event broker has reached its ACL profile limit. " +
            "Please contact Solace or remove unused ACL profiles to free capacity.",
            LogLevel.WARN
    ),
    INTERNAL_ERROR(
            "ema",
            1,
            Msg.INTERNAL_ERROR,
            "The Event Management Agent encountered an unexpected error. " +
            Msg.CONTACT_SUPPORT,
            LogLevel.ERROR
    );

    private final String serviceId;
    private final int code;
    private final String result;
    private final String userMessage;
    private final LogLevel logLevel;

    public enum LogLevel {
        WARN, ERROR
    }

    static final class Msg {
        static final String INTERNAL_ERROR = "INTERNAL_ERROR";
        static final String CONTACT_SUPPORT = "Contact support if the issue persists.";
        private Msg() {}
    }

    EmaErrorCode(String serviceId, int code, String result, String userMessage, LogLevel logLevel) {
        this.serviceId = serviceId;
        this.code = code;
        this.result = result;
        this.userMessage = userMessage;
        this.logLevel = logLevel;
    }

    public String getServiceId() {
        return serviceId;
    }

    public int getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public static EmaErrorCode fromName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public EmaInternalPayload buildInternal(String correlationId, String rawErrorMessage) {
        return EmaInternalPayload.builder()
                .correlationId(correlationId)
                .serviceId(serviceId)
                .code(code)
                .result(result)
                .errorMessage(rawErrorMessage)
                .build();
    }
}

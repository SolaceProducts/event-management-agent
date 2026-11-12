package com.solace.maas.ep.event.management.agent.command;

/**
 * EMA error code registry per the inter-microservice error protocol.
 * Maps each error condition to a numeric code, global result, safe user-facing message,
 * and the log level EMA should use when this error occurs.
 * Code is coarse-grained per protocol: 0 = success, 1 = failure. Consumers branch on result, not code.
 * See: https://sol-jira.atlassian.net/wiki/spaces/EventInteg/pages/6467813380
 *
 * userMessage: a null userMessage means the error is not user-actionable; the caller (ep-core) will
 * fall back to its own canned message. Only set a non-null message when the user can take a specific action.
 */
public enum EmaErrorCode {

    ACL_PROFILE_LIMIT_EXCEEDED(
            "ema",
            1,
            "LIMIT_EXCEEDED",
            "The event broker has reached its ACL profile limit. " +
            "Remove unused ACL profiles to free capacity.",
            true,
            LogLevel.WARN
    ),
    INTERNAL_ERROR(
            "ema",
            1,
            Msg.INTERNAL_ERROR,
            null,
            false,
            LogLevel.ERROR
    );

    private final String serviceId;
    private final int code;
    private final String result;
    private final String userMessage;
    private final boolean userActionable;
    private final LogLevel logLevel;

    public enum LogLevel {
        WARN, ERROR
    }

    static final class Msg {
        static final String INTERNAL_ERROR = "INTERNAL_ERROR";
        private Msg() {}
    }

    EmaErrorCode(String serviceId, int code, String result, String userMessage, boolean userActionable, LogLevel logLevel) {
        this.serviceId = serviceId;
        this.code = code;
        this.result = result;
        this.userMessage = userMessage;
        this.userActionable = userActionable;
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

    public boolean isUserActionable() {
        return userActionable;
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
        } catch (IllegalArgumentException _) {
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

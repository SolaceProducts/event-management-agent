package com.solace.maas.ep.event.management.agent.route.ep.exceptions;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

import java.util.List;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
public class ClientException extends RuntimeException {
    private String message;
    private Map<String, List<Exception>> exceptionStore;

    ClientException(final String message, final Map<String, List<Exception>> validationDetails) {
        this.message = message;
        this.exceptionStore = validationDetails;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Map<String, List<Exception>> getExceptionStore() {
        return this.exceptionStore;
    }

    public void setExceptionStore(final Map<String, List<Exception>> exceptionStore) {
        this.exceptionStore = exceptionStore;
    }

    public String toString() {
        String var10000 = this.getMessage();
        return "ClientException(message=" + var10000 + ", validationDetails=" + this.getExceptionStore() + ")";
    }
}

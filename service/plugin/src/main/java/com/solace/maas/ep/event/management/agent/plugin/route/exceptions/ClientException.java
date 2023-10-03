package com.solace.maas.ep.event.management.agent.plugin.route.exceptions;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@ExcludeFromJacocoGeneratedReport
@Builder
public class ClientException extends RuntimeException {
    private final String message;
    private final Map<String, List<Exception>> exceptionStore;

    ClientException(final String message, final Map<String, List<Exception>> validationDetails) {
        super();
        this.message = message;
        this.exceptionStore = validationDetails;
    }

    @Override
    public String toString() {
        String var10000 = this.getMessage();
        return "ClientException(message=" + var10000 + ", validationDetails=" + this.getExceptionStore() + ")";
    }
}

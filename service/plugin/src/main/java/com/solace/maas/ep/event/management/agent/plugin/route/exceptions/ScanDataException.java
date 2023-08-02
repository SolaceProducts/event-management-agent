package com.solace.maas.ep.event.management.agent.plugin.route.exceptions;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

import java.util.List;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
public class ScanDataException extends ClientException {
    private final String data;

    public ScanDataException(
            String message, Map<String, List<Exception>> validationDetails,
            String data) {
        super(message, validationDetails);
        this.data = data;
    }

    @Override
    public String toString() {
        return "ScanDataException{message=" + this.getMessage() +
                ", exceptionStore=" + this.getExceptionStore() +
                "data=" + data +
                '}';
    }
}
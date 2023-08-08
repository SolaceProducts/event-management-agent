package com.solace.maas.ep.event.management.agent.plugin.route.exceptions;

import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

import java.util.List;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
public class ScanOverallStatusException extends ClientException {
    private final String statusType;
    private final List<String> scanTypes;
    private final ScanStatus scanStatus;

    public ScanOverallStatusException(
            String message, Map<String, List<Exception>> validationDetails,
            String statusType, List<String> scanTypes, ScanStatus scanStatus) {
        super(message, validationDetails);
        this.statusType = statusType;
        this.scanTypes = scanTypes;
        this.scanStatus = scanStatus;
    }

    @Override
    public String toString() {
        return "ScanStatusException{message=" + this.getMessage() +
                ", exceptionStore=" + this.getExceptionStore() +
                ", statusType='" + statusType + '\'' +
                ", scanType='" + scanTypes + '\'' +
                ", scanStatus=" + scanStatus +
                '}';
    }
}
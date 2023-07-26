package com.solace.maas.ep.event.management.agent.route.ep.exceptions;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;

import java.util.List;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
public class ConfigurationTaskStatusException extends ClientException {
    private final String statusType;
    private final List<String> configTypes;
    private final TaskResult taskResult;

    public ConfigurationTaskStatusException(
            String message, Map<String, List<Exception>> validationDetails,
            String statusType, List<String> scanTypes, TaskResult taskResult) {
        super(message, validationDetails);
        this.statusType = statusType;
        configTypes = scanTypes;
        this.taskResult = taskResult;
    }

    @Override
    public String toString() {
        return "ConfigurationTaskStatusException{message=" + getMessage() +
                ", exceptionStore=" + getExceptionStore() +
                ", statusType='" + statusType + '\'' +
                ", configTypes='" + configTypes + '\'' +
                ", taskResult=" + taskResult.toString() +
                '}';
    }
}
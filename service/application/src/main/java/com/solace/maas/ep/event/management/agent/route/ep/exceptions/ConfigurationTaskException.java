package com.solace.maas.ep.event.management.agent.route.ep.exceptions;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;

import java.util.List;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
public class ConfigurationTaskException extends ClientException {
    private final List<TaskResult> data;

    public ConfigurationTaskException(
            String message, Map<String, List<Exception>> validationDetails,
            List<TaskResult> data) {
        super(message, validationDetails);
        this.data = data;
    }

    @Override
    public String toString() {
        return "ConfigurationTaskException{message=" + this.getMessage() +
                ", exceptionStore=" + this.getExceptionStore() +
                "data=" + data +
                '}';
    }
}
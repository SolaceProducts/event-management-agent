package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.common.model.ConfigDestination;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.Data;

import java.util.List;

@ExcludeFromJacocoGeneratedReport
@Data
public class ConfigurationTaskResultMessage extends MOPMessage {

    private String configType;
    private String messagingServiceId;
    private String taskId;
    private List<TaskResult> taskResults;

    private List<ConfigDestination> destinations;

    public ConfigurationTaskResultMessage() {
        super();
    }

    public ConfigurationTaskResultMessage(String messagingServiceId,
                                          String configType,
                                          String taskId,
                                          List<TaskResult> taskResults,
                                          List<ConfigDestination> destinations) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.messagingServiceId = messagingServiceId;
        this.taskId = taskId;
        this.taskResults = taskResults;
        this.destinations = destinations;
        this.configType = configType;
    }

    @Override
    public String toLog() {
        return null;
    }
}

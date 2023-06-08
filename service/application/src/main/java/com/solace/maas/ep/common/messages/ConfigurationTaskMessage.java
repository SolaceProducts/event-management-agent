package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.common.model.ConfigDestination;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

import java.util.List;

@ExcludeFromJacocoGeneratedReport
@Data
public class ConfigurationTaskMessage<Y> extends MOPMessage {

    private String configType;
    private String messagingServiceId;
    private String taskId;
    private List<TaskConfig<Y>> taskConfigs;

    private List<ConfigDestination> destinations;

    public ConfigurationTaskMessage() {
        super();
    }

    public ConfigurationTaskMessage(String messagingServiceId,
                                    String configType,
                                    String taskId,
                                    List<TaskConfig<Y>> taskConfigs,
                                    List<ConfigDestination> destinations) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.messagingServiceId = messagingServiceId;
        this.taskId = taskId;
        this.taskConfigs = taskConfigs;
        this.destinations = destinations;
        this.configType = configType;
    }
}

package com.solace.maas.ep.event.management.agent.configurationTaskManager.model;

import com.solace.maas.ep.event.management.agent.model.AbstractBaseBO;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ConfigurationTaskBO extends AbstractBaseBO<String> {
    private String id;
    private String messagingServiceId;
    private String messagingServiceName;
    private String messagingServiceType;
    private String emaId;
    private String configType;
    private List<String> destinations;
    private List<TaskConfig<?>> taskConfigs;
    private Instant createdAt;
}

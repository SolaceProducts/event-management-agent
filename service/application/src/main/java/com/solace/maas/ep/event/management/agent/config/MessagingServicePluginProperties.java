package com.solace.maas.ep.event.management.agent.config;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceManagementProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagingServicePluginProperties implements Serializable {
    private String id;

    private String name;

    private String type;

    private MessagingServiceManagementProperties management;

    private Map<String, Object> properties;
}

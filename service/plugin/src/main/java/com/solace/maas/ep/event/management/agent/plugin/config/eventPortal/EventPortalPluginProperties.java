package com.solace.maas.ep.event.management.agent.plugin.config.eventPortal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "event-portal")
public class EventPortalPluginProperties {
    private String runtimeAgentId;

    private String organizationId;

    private String topicPrefix;

    private GatewayProperties gateway;
}

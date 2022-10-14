package com.solace.maas.ep.event.management.agent.config.eventPortal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "event-portal")
public class EventPortalProperties {
    private String runtimeAgentId;

    private String organizationId;

    private String topicPrefix;

    private GatewayProperties gateway;
}

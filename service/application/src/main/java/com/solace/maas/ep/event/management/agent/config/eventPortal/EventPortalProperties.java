package com.solace.maas.ep.event.management.agent.config.eventPortal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "event-portal")
public class EventPortalProperties {
    private String runtimeAgentId = "standalone";

    private String organizationId = "standalone";

    private String topicPrefix;

    private int commandThreadPoolMinSize = 5;
    private int commandThreadPoolMaxSize = 10;
    private int commandThreadPoolQueueSize = 1_000;

    private GatewayProperties gateway
            = new GatewayProperties("standalone", "standalone", new GatewayMessagingProperties(true, false, List.of()));
}

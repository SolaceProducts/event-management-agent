package com.solace.maas.ep.event.management.agent.plugin.solace.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "solace-messaging")
@Data
public class SolaceMessagingProperties {
    private String url;
    private String msgVpnName;
    private String username;
    private String password;
}

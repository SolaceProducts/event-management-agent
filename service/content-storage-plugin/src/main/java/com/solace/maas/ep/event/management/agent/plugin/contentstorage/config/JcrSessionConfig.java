package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Data
@Configuration
@ConfigurationProperties(prefix = "ema.content.jcr")
@ConditionalOnProperty(name = "ema.content.enabled", havingValue = "true")
public class JcrSessionConfig {
    private String username;

    private String password;
}

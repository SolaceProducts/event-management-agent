package com.solace.maas.ep.runtime.agent.plugin.properties;

import com.solace.maas.ep.runtime.agent.plugin.messagingService.MessagingServiceProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@PropertySource("classpath:application.yml")
@Configuration
@ConfigurationProperties(prefix = "plugins.solace")
public class SolacePluginProperties {
    private Semp semp;
    private List<MessagingServiceProperties> messagingServices;
}
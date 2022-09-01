package com.solace.maas.ep.runtime.agent.config;

import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.manager.client.KafkaClientManagerImpl;
import com.solace.maas.ep.runtime.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.runtime.agent.plugin.manager.client.SolaceSempClientManagerImpl;
import com.solace.maas.ep.runtime.agent.plugin.properties.SolacePluginProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ExcludeFromJacocoGeneratedReport
@Configuration
public class MessagingServiceTypeConfig {
    private final ConcurrentHashMap<String, MessagingServiceClientManager<?>> messagingServiceManagersMap;

    @Autowired
    public MessagingServiceTypeConfig() {
        messagingServiceManagersMap = new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, MessagingServiceClientManager<?>> messagingServiceManagers(SolacePluginProperties solaceProperties) {
        messagingServiceManagersMap.put(MessagingServiceType.KAFKA.name(), new KafkaClientManagerImpl());
        messagingServiceManagersMap.put(MessagingServiceType.SOLACE.name(),
                new SolaceSempClientManagerImpl(solaceProperties)
        );

        return messagingServiceManagersMap;
    }
}

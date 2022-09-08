package com.solace.maas.ep.runtime.agent.plugin.config;

import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.manager.client.MessagingServiceClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ExcludeFromJacocoGeneratedReport
@Configuration
public class MessagingServiceTypeConfig {
    @Autowired
    public MessagingServiceTypeConfig() {

    }

    @Bean
    public Map<String, MessagingServiceClientManager<?>> messagingServiceManagers() {
        return new ConcurrentHashMap<>();
    }
}

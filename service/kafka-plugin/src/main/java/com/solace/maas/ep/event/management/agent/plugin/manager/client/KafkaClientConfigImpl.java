package com.solace.maas.ep.event.management.agent.plugin.manager.client;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaClientConfigImpl extends MessagingServiceClientConfig {
    protected KafkaClientConfigImpl() {
        super("KAFKA", new KafkaClientManagerImpl());
    }
}

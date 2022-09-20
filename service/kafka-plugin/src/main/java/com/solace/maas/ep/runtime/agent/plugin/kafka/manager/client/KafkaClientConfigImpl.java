package com.solace.maas.ep.runtime.agent.plugin.kafka.manager.client;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaClientConfigImpl extends MessagingServiceClientConfig {
    protected KafkaClientConfigImpl() {
        super("KAFKA", new KafkaClientManagerImpl());
    }
}

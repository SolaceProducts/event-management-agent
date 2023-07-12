package com.solace.maas.ep.event.management.agent.plugin.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaClientConfigImpl extends MessagingServiceClientConfig {
    protected KafkaClientConfigImpl(KafkaClientConfig kafkaClientConfig) {
        super("KAFKA", new KafkaClientManagerImpl(kafkaClientConfig));
    }
}

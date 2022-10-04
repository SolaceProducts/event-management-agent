package com.solace.maas.ep.event.management.agent.plugin.rabbitmq.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.kafka.manager.client.MessagingServiceClientConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqClientConfigImpl extends MessagingServiceClientConfig {
    protected RabbitMqClientConfigImpl() {
        super("RABBITMQ", new RabbitMqClientManagerImpl());
    }
}

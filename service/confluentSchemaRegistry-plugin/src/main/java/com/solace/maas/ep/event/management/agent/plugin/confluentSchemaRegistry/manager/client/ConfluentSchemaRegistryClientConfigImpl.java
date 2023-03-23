package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfluentSchemaRegistryClientConfigImpl extends MessagingServiceClientConfig {
    protected ConfluentSchemaRegistryClientConfigImpl() {
        super("CONFLUENT_SCHEMA_REGISTRY", new ConfluentSchemaRegistryClientManagerImpl());
    }
}

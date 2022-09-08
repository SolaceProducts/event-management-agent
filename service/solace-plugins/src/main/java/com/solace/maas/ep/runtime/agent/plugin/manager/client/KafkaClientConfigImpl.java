package com.solace.maas.ep.runtime.agent.plugin.manager.client;

import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaClientConfigImpl extends MessagingServiceClientConfig {
    protected KafkaClientConfigImpl(Map<String, MessagingServiceClientManager<?>> messagingServiceManagers) {
        super(messagingServiceManagers, MessagingServiceType.KAFKA.name(), new KafkaClientManagerImpl());
    }
}

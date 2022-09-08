package com.solace.maas.ep.runtime.agent.plugin.kafka.manager.client;

import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.properties.SolacePluginProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceClientConfigImpl extends MessagingServiceClientConfig {
    protected SolaceClientConfigImpl(SolacePluginProperties solaceProperties) {
        super(MessagingServiceType.SOLACE.name(), new SolaceSempClientManagerImpl(solaceProperties));
    }
}

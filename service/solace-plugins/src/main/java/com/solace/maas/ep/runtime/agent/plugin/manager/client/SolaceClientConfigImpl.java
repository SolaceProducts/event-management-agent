package com.solace.maas.ep.runtime.agent.plugin.manager.client;

import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.properties.SolacePluginProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SolaceClientConfigImpl extends MessagingServiceClientConfig {
    protected SolaceClientConfigImpl(Map<String, MessagingServiceClientManager<?>> messagingServiceManagers,
                                     SolacePluginProperties solaceProperties) {
        super(messagingServiceManagers, MessagingServiceType.SOLACE.name(),
                new SolaceSempClientManagerImpl(solaceProperties));
    }
}

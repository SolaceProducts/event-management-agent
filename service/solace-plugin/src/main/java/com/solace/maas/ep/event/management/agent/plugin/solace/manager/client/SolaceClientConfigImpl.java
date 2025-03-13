package com.solace.maas.ep.event.management.agent.plugin.solace.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.EventPortalPluginProperties;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceClientConfigImpl extends MessagingServiceClientConfig {

    protected SolaceClientConfigImpl(EventPortalPluginProperties properties) {
        super("SOLACE", new SolaceSempClientManagerImpl(properties));
    }
}

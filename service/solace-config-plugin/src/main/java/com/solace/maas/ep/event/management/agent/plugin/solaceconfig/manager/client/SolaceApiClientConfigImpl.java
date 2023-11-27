package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceApiClientConfigImpl extends MessagingServiceClientConfig {
    protected SolaceApiClientConfigImpl() {
        super("SOLACE_CONFIG", new SolaceSempApiClientManagerImpl());
    }
}

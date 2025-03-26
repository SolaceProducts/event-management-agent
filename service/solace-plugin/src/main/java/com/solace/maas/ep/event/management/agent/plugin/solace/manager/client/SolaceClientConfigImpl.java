package com.solace.maas.ep.event.management.agent.plugin.solace.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceClientConfigImpl extends MessagingServiceClientConfig {

    protected SolaceClientConfigImpl() {
        super("SOLACE", new SolaceSempClientManagerImpl());
    }
}

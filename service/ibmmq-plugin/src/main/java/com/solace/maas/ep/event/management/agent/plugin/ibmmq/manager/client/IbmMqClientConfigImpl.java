package com.solace.maas.ep.event.management.agent.plugin.ibmmq.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IbmMqClientConfigImpl extends MessagingServiceClientConfig {

    private static final String SERVICE_NAME = "IBMMQ";

    protected IbmMqClientConfigImpl() {
        super(SERVICE_NAME, new IbmMqClientManagerImpl());
    }

}

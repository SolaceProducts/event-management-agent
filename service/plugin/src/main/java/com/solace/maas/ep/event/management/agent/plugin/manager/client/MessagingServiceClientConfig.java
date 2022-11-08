package com.solace.maas.ep.event.management.agent.plugin.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.config.MessagingServiceTypeConfig;

public abstract class MessagingServiceClientConfig {

    protected MessagingServiceClientConfig(String serviceName, MessagingServiceClientManager<?> clientManager) {
        MessagingServiceTypeConfig.addMessagingServiceManager(serviceName, clientManager);
    }
}

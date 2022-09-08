package com.solace.maas.ep.runtime.agent.plugin.manager.client;

import java.util.Map;

public abstract class MessagingServiceClientConfig {
    private final Map<String, MessagingServiceClientManager<?>> messagingServiceManagers;

    protected MessagingServiceClientConfig(
            Map<String, MessagingServiceClientManager<?>> messagingServiceManagers,
            String serviceName, MessagingServiceClientManager<?> clientManager) {
        this.messagingServiceManagers = messagingServiceManagers;

        messagingServiceManagers.put(serviceName, clientManager);
    }
}

package com.solace.maas.ep.runtime.agent.plugin.config;

import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.kafka.manager.client.MessagingServiceClientManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ExcludeFromJacocoGeneratedReport
public class MessagingServiceTypeConfig {
    private final static Map<String, MessagingServiceClientManager<?>> messagingServiceManagers =
            new ConcurrentHashMap<>();

    public static void addMessagingServiceManager(String serviceName, MessagingServiceClientManager<?> clientManager) {
        messagingServiceManagers.put(serviceName, clientManager);
    }

    public static Map<String, MessagingServiceClientManager<?>> getMessagingServiceManagers() {
        return messagingServiceManagers;
    }
}

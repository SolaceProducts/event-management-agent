package com.solace.maas.ep.event.management.agent.plugin.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;

public interface MessagingServiceClientManager<T> {
        T getClient(ConnectionDetailsEvent connectionDetailsEvent);
}

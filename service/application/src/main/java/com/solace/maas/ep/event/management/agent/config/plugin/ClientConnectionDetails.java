package com.solace.maas.ep.event.management.agent.config.plugin;

import com.solace.maas.ep.event.management.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceConnectionProperties;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;

public interface ClientConnectionDetails {
    ConnectionDetailsEvent createConnectionDetails(String messagingServiceId,
                                                   MessagingServiceConnectionProperties messagingServiceConnection,
                                                   MessagingServiceType messagingServiceType);
}

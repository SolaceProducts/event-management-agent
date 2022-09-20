package com.solace.maas.ep.runtime.agent.config.plugin;

import com.solace.maas.ep.runtime.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.MessagingServiceConnectionProperties;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.ConnectionDetailsEvent;

public interface ClientConnectionDetails {
    ConnectionDetailsEvent createConnectionDetails(String messagingServiceId,
                                                   MessagingServiceConnectionProperties messagingServiceConnection,
                                                   MessagingServiceType messagingServiceType);
}

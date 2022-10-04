package com.solace.maas.ep.event.management.agent.plugin.config.eventPortal;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceConnectionProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayMessagingProperties {
    private boolean standalone;

    private boolean rtoSession;

    private List<MessagingServiceConnectionProperties> connections;
}

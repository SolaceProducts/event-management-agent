package com.solace.maas.ep.event.management.agent.plugin.config.eventPortal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayProperties {
    private String id;

    private String name;

    private GatewayMessagingProperties messaging;
}

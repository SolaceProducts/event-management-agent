package com.solace.maas.ep.event.management.agent.plugin.messagingService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConnectionDetailsEvent implements Serializable {
    private String id;
    private String messagingServiceId;

    private String name;

    private String url;

    private List<AuthenticationDetailsEvent> authenticationDetails;

    private List<EventProperty> properties;
}

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
public class CredentialDetailsEvent implements Serializable {
    private String id;
    private String type;
    private String source;
    private List<AuthenticationOperationDetailsEvent> operations;
    private List<EventProperty> properties;
}

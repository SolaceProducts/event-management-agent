package com.solace.maas.ep.event.management.agent.plugin.messagingService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDetailsEvent implements Serializable {
    private String id;

//    private String username;

//    private String password;

    private String protocol;

//    private String authType;

    private List<CredentialDetailsEvent> credentials;

    private List<EventProperty> properties;

    //    private List<AuthenticationOperationDetailsEvent> operations;
}

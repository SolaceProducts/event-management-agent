package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.common.model.EventBrokerAuthenticationConfiguration;
import com.solace.maas.ep.common.model.EventBrokerConnectionConfiguration;
import com.solace.maas.ep.common.model.EventBrokerCredentialConfiguration;
import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationOperationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Service
@Validated
public class SolaceResourceConfigurationToEventConverter {

    public MessagingServiceEvent mapToMessagingServiceEvent(@Valid EventBrokerResourceConfiguration eventBrokerResource) {
        Objects.requireNonNull(eventBrokerResource, "eventBrokerResource cannot be null.");
        MessagingServiceEvent serviceEvent = new MessagingServiceEvent();
        serviceEvent.setName(eventBrokerResource.getName());
        serviceEvent.setId(eventBrokerResource.getId());
        serviceEvent.setMessagingServiceType(eventBrokerResource.getResourceConfigurationType().name());

        ConnectionDetailsEvent connectionDetailsEvent = new ConnectionDetailsEvent();
        EventBrokerConnectionConfiguration connectionDetail = eventBrokerResource.getConnections().get(0);

        connectionDetailsEvent.setName(eventBrokerResource.getName());
        connectionDetailsEvent.setUrl(connectionDetail.getUrl());
        connectionDetailsEvent.setMessagingServiceId(eventBrokerResource.getId());
        connectionDetailsEvent.setProperties(List.of(
                EventProperty.builder()
                        .name("sempPageSize")
                        .value(StringUtils.isEmpty(connectionDetail.getSempPageSize()) ? "100" : connectionDetail.getSempPageSize())
                        .build(),
                EventProperty.builder()
                        .name("msgVpn")
                        .value(connectionDetail.getMsgVpn())
                        .build())

        );
        EventBrokerAuthenticationConfiguration authentication = connectionDetail.getAuthentication();
        AuthenticationDetailsEvent authEvent = new AuthenticationDetailsEvent();
        authEvent.setProtocol(StringUtils.isEmpty(authentication.getProtocol()) ? "semp" : authentication.getProtocol());
        authEvent.setProperties(List.of(
                EventProperty.builder()
                        .name("type")
                        .value(StringUtils.isEmpty(authentication.getType()) ? "basicAuthentication" : authentication.getType())
                        .build()
        ));

        CredentialDetailsEvent credentialDetailsEvent = new CredentialDetailsEvent();
        EventBrokerCredentialConfiguration c = authentication.getCredential();
        credentialDetailsEvent.setSource("ENVIRONMENT_VARIABLE");
        credentialDetailsEvent.setOperations(List.of(AuthenticationOperationDetailsEvent.builder()
                .name("ALL")
                .build()));
        credentialDetailsEvent.setProperties(
                List.of(
                        EventProperty.builder()
                                .name("username")
                                .value(c.getUserName())
                                .build(),
                        EventProperty.builder()
                                .name("password")
                                .value(c.getPassword())
                                .build()

                )
        );
        authEvent.setCredentials(
                List.of(credentialDetailsEvent)

        );
        connectionDetailsEvent.setAuthenticationDetails(List.of(authEvent));
        serviceEvent.setConnectionDetails(List.of(connectionDetailsEvent));
        return serviceEvent;
    }
}

package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationOperationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.AuthenticationDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessagingServiceEntityToEventConverter extends MessagingServiceConverter {
    public MessagingServiceEvent convert(MessagingServiceEntity messagingService) {
        List<ConnectionDetailsEvent> connectionDetails = new ArrayList<>();

        messagingService.getConnections()
                .forEach(messagingServiceConnection -> {
                    List<AuthenticationDetailsEvent> authenticationDetailsEvents =
                            ensureList(messagingServiceConnection.getAuthentication()).stream()
                                    .map(this::convertAuthenticationDetailsEntityToEvent)
                                    .collect(Collectors.toUnmodifiableList());

                    ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                            .id(messagingServiceConnection.getId())
                            .name(messagingServiceConnection.getName())
                            .url(messagingServiceConnection.getUrl())
                            .properties(ensureList(messagingServiceConnection.getProperties()).stream()
                                    .map(this::convertPropertyEntityToEvent)
                                    .collect(Collectors.toUnmodifiableList()))
                            .authenticationDetails(authenticationDetailsEvents)
                            .build();

                    connectionDetails.add(connectionDetailsEvent);
                });

        return MessagingServiceEvent.builder()
                .id(messagingService.getId())
                .name(messagingService.getName())
                .messagingServiceType(messagingService.getType())
                .connectionDetails(connectionDetails)
                .build();
    }

    private AuthenticationDetailsEvent convertAuthenticationDetailsEntityToEvent(AuthenticationDetailsEntity authenticationDetailsEntity) {
        return AuthenticationDetailsEvent.builder()
                .id(authenticationDetailsEntity.getId())
                .protocol(authenticationDetailsEntity.getProtocol())
                .properties(ensureList(authenticationDetailsEntity.getProperties()).stream()
                        .map(this::convertPropertyEntityToEvent)
                        .collect(Collectors.toUnmodifiableList()))
                .credentials(ensureList(authenticationDetailsEntity.getCredentials()).stream()
                        .map(credentialDetailsEntity -> CredentialDetailsEvent.builder()
                                .id(credentialDetailsEntity.getId())
                                .source(credentialDetailsEntity.getSource())
                                .operations(ensureList(credentialDetailsEntity.getOperations()).stream()
                                        .map(opEntity -> AuthenticationOperationDetailsEvent.builder()
                                                .name(opEntity.getName())
                                                .build())
                                        .collect(Collectors.toUnmodifiableList()))
                                .properties(ensureList(credentialDetailsEntity.getProperties()).stream()
                                        .map(this::convertPropertyEntityToEvent)
                                        .collect(Collectors.toUnmodifiableList()))
                                .build())
                        .collect(Collectors.toUnmodifiableList()))
                .build();
    }
}

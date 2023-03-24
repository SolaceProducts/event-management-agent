package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.config.AuthenticationDetailsPluginProperties;
import com.solace.maas.ep.event.management.agent.config.AuthenticationOperationDetailsProperties;
import com.solace.maas.ep.event.management.agent.config.CredentialDetailsProperties;
import com.solace.maas.ep.event.management.agent.config.MessagingServiceConnectionPluginProperties;
import com.solace.maas.ep.event.management.agent.config.MessagingServicePluginProperties;
import com.solace.maas.ep.event.management.agent.config.PluginProperties;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationOperationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessagingServicePluginPropertyToEventConverter {
    public MessagingServiceEvent convert(MessagingServicePluginProperties messagingService) {
        List<ConnectionDetailsEvent> connectionDetails = new ArrayList<>();

        Objects.requireNonNullElse(messagingService.getConnections(), new ArrayList<MessagingServiceConnectionPluginProperties>())
                .forEach(messagingServiceConnection -> {
                    List<AuthenticationDetailsEvent> authenticationDetailsEvents =
                            Objects.requireNonNullElse(messagingServiceConnection.getAuthentication(),
                                            new ArrayList<AuthenticationDetailsPluginProperties>())
                                    .stream()
                                    .map(this::convertAuthenticationDetailsConfigToEvent)
                                    .collect(Collectors.toUnmodifiableList());

                    ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                            .name(messagingServiceConnection.getName())
                            .url(messagingServiceConnection.getUrl())
                            .messagingServiceId(messagingServiceConnection.getMessagingServiceId() == null ?
                                    null : messagingService.getId())
                            .properties(Objects.requireNonNullElse(messagingServiceConnection.getProperties(),
                                            new ArrayList<PluginProperties>())
                                    .stream()
                                    .map(this::convertPropertyConfigToEvent)
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

    private AuthenticationDetailsEvent convertAuthenticationDetailsConfigToEvent(
            AuthenticationDetailsPluginProperties authenticationDetailsConfig) {
        return AuthenticationDetailsEvent.builder()
                .protocol(authenticationDetailsConfig.getProtocol())
                .properties(Objects.requireNonNullElse(authenticationDetailsConfig.getProperties(),
                                new ArrayList<PluginProperties>())
                        .stream()
                        .map(this::convertPropertyConfigToEvent)
                        .collect(Collectors.toUnmodifiableList()))
                .credentials(Objects.requireNonNullElse(authenticationDetailsConfig.getCredentials(),
                                new ArrayList<CredentialDetailsProperties>())
                        .stream()
                        .map(credentialDetailsEntity -> CredentialDetailsEvent.builder()
                                .source(credentialDetailsEntity.getSource())
                                .operations(Objects.requireNonNullElse(credentialDetailsEntity.getOperations(),
                                                new ArrayList<AuthenticationOperationDetailsProperties>())
                                        .stream()
                                        .map(opEntity -> AuthenticationOperationDetailsEvent.builder()
                                                .name(opEntity.getName())
                                                .build())
                                        .collect(Collectors.toUnmodifiableList()))
                                .properties(Objects.requireNonNullElse(credentialDetailsEntity.getProperties(),
                                                new ArrayList<PluginProperties>())
                                        .stream()
                                        .map(this::convertPropertyConfigToEvent)
                                        .collect(Collectors.toUnmodifiableList()))
                                .build())
                        .collect(Collectors.toUnmodifiableList()))
                .build();
    }

    public EventProperty convertPropertyConfigToEvent(PluginProperties property) {
        return EventProperty.builder()
                .name(property.getName())
                .value(property.getValue())
                .build();
    }
}

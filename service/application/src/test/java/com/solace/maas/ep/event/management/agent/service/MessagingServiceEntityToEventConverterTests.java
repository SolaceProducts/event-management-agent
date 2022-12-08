package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationOperationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.AuthenticationDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialOperationsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MessagingServiceEntityToEventConverterTests {
    private final MessagingServiceEventToEntityConverter messagingServiceEventToEntityConverter =
            new MessagingServiceEventToEntityConverter();

    @Test
    public void testEventToEntity() {
        MessagingServiceEvent messagingServiceEvent = MessagingServiceEvent.builder()
                .id("messaging_service_id")
                .messagingServiceType("TEST")
                .name("messaging service name")
                .build();

        ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                .messagingServiceId(messagingServiceEvent.getId())
                .name("connection name")
                .id("connection_id")
                .properties(List.of(EventProperty.builder()
                        .name("connection_prop")
                        .value("connection_value")
                        .build()))
                .build();

        AuthenticationDetailsEvent authenticationDetailsEvent = AuthenticationDetailsEvent.builder()
                .id("auth_id")
                .protocol("SEMP")
                .properties(List.of(EventProperty.builder()
                        .name("auth_prop")
                        .value("auth_value")
                        .build()))
                .build();

        CredentialDetailsEvent credentialDetailsEvent = CredentialDetailsEvent.builder()
                .id("cred_id")
                .properties(List.of(EventProperty.builder()
                        .name("cred_prop")
                        .value("cred_value")
                        .build()))
                .build();

        AuthenticationOperationDetailsEvent credentialOperationsEvent = AuthenticationOperationDetailsEvent.builder()
                .id("op_id")
                .name("op_name")
                .build();

        credentialDetailsEvent.setOperations(List.of(credentialOperationsEvent));
        authenticationDetailsEvent.setCredentials(List.of(credentialDetailsEvent));
        connectionDetailsEvent.setAuthenticationDetails(List.of(authenticationDetailsEvent));
        messagingServiceEvent.setConnectionDetails(List.of(connectionDetailsEvent));

        MessagingServiceEntity messagingServiceEntity = messagingServiceEventToEntityConverter.convert(messagingServiceEvent);
        assertThat(messagingServiceEntity.getId()).isEqualTo(messagingServiceEvent.getId());
        assertThat(messagingServiceEntity.getType()).isEqualTo(messagingServiceEvent.getMessagingServiceType());
        assertThat(messagingServiceEntity.getName()).isEqualTo(messagingServiceEvent.getName());

        assertThat(messagingServiceEntity.getConnections().size()).isEqualTo(1);
        ConnectionDetailsEntity connectionDetailsEntity = messagingServiceEntity.getConnections().get(0);
        assertThat(connectionDetailsEntity.getId()).isEqualTo(connectionDetailsEvent.getId());
        assertThat(connectionDetailsEntity.getMessagingService().getId()).isEqualTo(connectionDetailsEvent.getMessagingServiceId());
        assertThat(connectionDetailsEntity.getName()).isEqualTo(connectionDetailsEvent.getName());
        assertThat(connectionDetailsEntity.getUrl()).isEqualTo(connectionDetailsEvent.getUrl());
        assertThat(connectionDetailsEntity.getProperties().size()).isEqualTo(1);
        assertThat(connectionDetailsEntity.getProperties().get(0).getName())
                .isEqualTo(connectionDetailsEvent.getProperties().get(0).getName());
        assertThat(connectionDetailsEntity.getProperties().get(0).getValue())
                .isEqualTo(connectionDetailsEvent.getProperties().get(0).getValue());

        assertThat(connectionDetailsEntity.getAuthentication().size()).isEqualTo(1);
        AuthenticationDetailsEntity authenticationDetailsEntity = connectionDetailsEntity.getAuthentication().get(0);
        assertThat(authenticationDetailsEntity.getId()).isEqualTo(authenticationDetailsEvent.getId());
        assertThat(authenticationDetailsEntity.getProtocol()).isEqualTo(authenticationDetailsEvent.getProtocol());
        assertThat(authenticationDetailsEntity.getProperties().size()).isEqualTo(1);
        assertThat(authenticationDetailsEntity.getProperties().get(0).getName())
                .isEqualTo(authenticationDetailsEvent.getProperties().get(0).getName());
        assertThat(authenticationDetailsEntity.getProperties().get(0).getValue())
                .isEqualTo(authenticationDetailsEvent.getProperties().get(0).getValue());

        assertThat(authenticationDetailsEntity.getCredentials().size()).isEqualTo(1);
        CredentialDetailsEntity credentialDetailsEntity = authenticationDetailsEntity.getCredentials().get(0);
        assertThat(credentialDetailsEntity.getId()).isEqualTo(credentialDetailsEvent.getId());
        assertThat(credentialDetailsEntity.getProperties().size()).isEqualTo(1);
        assertThat(credentialDetailsEntity.getProperties().get(0).getName())
                .isEqualTo(credentialDetailsEvent.getProperties().get(0).getName());
        assertThat(credentialDetailsEntity.getProperties().get(0).getValue())
                .isEqualTo(credentialDetailsEvent.getProperties().get(0).getValue());

        assertThat(credentialDetailsEntity.getOperations().size()).isEqualTo(1);
        CredentialOperationsEntity credentialOperationsEntity = credentialDetailsEntity.getOperations().get(0);
        assertThat(credentialOperationsEntity.getId()).isEqualTo(credentialOperationsEvent.getId());
        assertThat(credentialOperationsEntity.getName()).isEqualTo(credentialOperationsEvent.getName());
    }
}

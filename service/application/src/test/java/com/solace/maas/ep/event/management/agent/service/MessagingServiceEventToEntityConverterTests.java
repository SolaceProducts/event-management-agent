package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationOperationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.AuthenticationDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.AuthenticationPropertiesEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionPropertiesEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialOperationsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialPropertiesEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MessagingServiceEventToEntityConverterTests {
    private final MessagingServiceEntityToEventConverter messagingServiceEntityToEventConverter =
            new MessagingServiceEntityToEventConverter();

    @Test
    public void testEntityToEvent() {
        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id("messaging_service_id")
                .type("TEST")
                .name("messaging service name")
                .build();

        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .messagingService(messagingServiceEntity)
                .name("connection name")
                .id("connection_id")
                .properties(List.of(ConnectionPropertiesEntity.builder()
                        .name("connection_prop")
                        .value("connection_value")
                        .build()))
                .build();

        AuthenticationDetailsEntity authenticationDetailsEntity = AuthenticationDetailsEntity.builder()
                .id("auth_id")
                .connections(connectionDetailsEntity)
                .protocol("SEMP")
                .properties(List.of(AuthenticationPropertiesEntity.builder()
                        .name("auth_prop")
                        .value("auth_value")
                        .build()))
                .build();

        CredentialDetailsEntity credentialDetailsEntity = CredentialDetailsEntity.builder()
                .id("cred_id")
                .authentication(authenticationDetailsEntity)
                .properties(List.of(CredentialPropertiesEntity.builder()
                        .name("cred_prop")
                        .value("cred_value")
                        .build()))
                .build();

        CredentialOperationsEntity credentialOperationsEntity = CredentialOperationsEntity.builder()
                .id("op_id")
                .credentials(credentialDetailsEntity)
                .name("op_name")
                .build();

        credentialDetailsEntity.setOperations(List.of(credentialOperationsEntity));
        authenticationDetailsEntity.setCredentials(List.of(credentialDetailsEntity));
        connectionDetailsEntity.setAuthentication(List.of(authenticationDetailsEntity));
        messagingServiceEntity.setConnections(List.of(connectionDetailsEntity));

        MessagingServiceEvent messagingServiceEvent = messagingServiceEntityToEventConverter.convert(messagingServiceEntity);
        assertThat(messagingServiceEvent.getId()).isEqualTo(messagingServiceEntity.getId());
        assertThat(messagingServiceEvent.getMessagingServiceType()).isEqualTo(messagingServiceEntity.getType());
        assertThat(messagingServiceEvent.getName()).isEqualTo(messagingServiceEntity.getName());

        assertThat(messagingServiceEvent.getConnectionDetails().size()).isEqualTo(1);
        ConnectionDetailsEvent connectionDetailsEvent = messagingServiceEvent.getConnectionDetails().get(0);
        assertThat(connectionDetailsEvent.getId()).isEqualTo(connectionDetailsEntity.getId());
        assertThat(connectionDetailsEvent.getMessagingServiceId()).isEqualTo(connectionDetailsEntity.getMessagingService().getId());
        assertThat(connectionDetailsEvent.getUrl()).isEqualTo(connectionDetailsEntity.getUrl());
        assertThat(connectionDetailsEvent.getName()).isEqualTo(connectionDetailsEntity.getName());
        assertThat(connectionDetailsEvent.getProperties().size()).isEqualTo(1);
        assertThat(connectionDetailsEvent.getProperties().get(0).getName())
                .isEqualTo(connectionDetailsEntity.getProperties().get(0).getName());
        assertThat(connectionDetailsEvent.getProperties().get(0).getValue())
                .isEqualTo(connectionDetailsEntity.getProperties().get(0).getValue());

        assertThat(connectionDetailsEvent.getAuthenticationDetails().size()).isEqualTo(1);
        AuthenticationDetailsEvent authenticationDetailsEvent = connectionDetailsEvent.getAuthenticationDetails().get(0);
        assertThat(authenticationDetailsEvent.getId()).isEqualTo(authenticationDetailsEntity.getId());
        assertThat(authenticationDetailsEvent.getProtocol()).isEqualTo(authenticationDetailsEntity.getProtocol());
        assertThat(authenticationDetailsEvent.getProperties().size()).isEqualTo(1);
        assertThat(authenticationDetailsEvent.getProperties().get(0).getName())
                .isEqualTo(authenticationDetailsEntity.getProperties().get(0).getName());
        assertThat(authenticationDetailsEvent.getProperties().get(0).getValue())
                .isEqualTo(authenticationDetailsEntity.getProperties().get(0).getValue());

        assertThat(authenticationDetailsEvent.getCredentials().size()).isEqualTo(1);
        CredentialDetailsEvent credentialDetailsEvent = authenticationDetailsEvent.getCredentials().get(0);
        assertThat(credentialDetailsEvent.getId()).isEqualTo(credentialDetailsEntity.getId());
        assertThat(credentialDetailsEvent.getProperties().size()).isEqualTo(1);
        assertThat(credentialDetailsEvent.getProperties().get(0).getName())
                .isEqualTo(credentialDetailsEntity.getProperties().get(0).getName());
        assertThat(credentialDetailsEvent.getProperties().get(0).getValue())
                .isEqualTo(credentialDetailsEntity.getProperties().get(0).getValue());

        assertThat(credentialDetailsEvent.getOperations().size()).isEqualTo(1);
        AuthenticationOperationDetailsEvent authenticationOperationDetailsEvent = credentialDetailsEvent.getOperations().get(0);
        assertThat(authenticationOperationDetailsEvent.getId()).isEqualTo(credentialOperationsEntity.getId());
        assertThat(authenticationOperationDetailsEvent.getName()).isEqualTo(credentialOperationsEntity.getName());
    }
}

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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MessagingServicePluginPropertyToEventConverterTest {

    private final MessagingServicePluginPropertyToEventConverter configToEventConverter =
            new MessagingServicePluginPropertyToEventConverter();

    @Test
    void testPluginPropertyToEvent() {
        MessagingServicePluginProperties messagingServicePluginProperties = MessagingServicePluginProperties.builder()
                .id("some-id")
                .name("some-name")
                .relatedServices(List.of("a", "b"))
                .type("some-type")
                .build();

        MessagingServiceConnectionPluginProperties messagingServiceConnectionPluginProperties = MessagingServiceConnectionPluginProperties.builder()
                .messagingServiceId(messagingServicePluginProperties.getId())
                .name("connection-name")
                .url("some-url")
                .properties(List.of(PluginProperties.builder()
                        .name("connection-prop-name")
                        .value("connection-prop-value")
                        .build()))
                .build();

        AuthenticationDetailsPluginProperties authenticationDetailsPluginProperties = AuthenticationDetailsPluginProperties.builder()
                .protocol("some-protocol")
                .properties(List.of(PluginProperties.builder()
                        .name("auth-prop-name")
                        .value("auth-prop-value")
                        .build()))
                .build();

        CredentialDetailsProperties credentialDetailsProperties = CredentialDetailsProperties.builder()
                .source("some-source")
                .properties(List.of(PluginProperties.builder()
                        .name("cred-prop-name")
                        .value("cred-prop-value")
                        .build()))
                .build();

        AuthenticationOperationDetailsProperties authenticationOperationDetailsProperties = AuthenticationOperationDetailsProperties.builder()
                .name("some-auth-op-name")
                .build();

        credentialDetailsProperties.setOperations(List.of(authenticationOperationDetailsProperties));
        authenticationDetailsPluginProperties.setCredentials(List.of(credentialDetailsProperties));
        messagingServiceConnectionPluginProperties.setAuthentication(List.of(authenticationDetailsPluginProperties));
        messagingServicePluginProperties.setConnections(List.of(messagingServiceConnectionPluginProperties));

        MessagingServiceEvent messagingServiceEvent = configToEventConverter.convert(messagingServicePluginProperties);

        assertThat(messagingServiceEvent.getMessagingServiceType()).isEqualTo(messagingServicePluginProperties.getType());
        assertThat(messagingServiceEvent.getName()).isEqualTo(messagingServicePluginProperties.getName());

        assertThat(messagingServiceEvent.getConnectionDetails()).hasSize(1);
        ConnectionDetailsEvent connectionDetailsEvent = messagingServiceEvent.getConnectionDetails().get(0);
        assertThat(connectionDetailsEvent.getMessagingServiceId()).isEqualTo(messagingServiceConnectionPluginProperties.getMessagingServiceId());
        assertThat(connectionDetailsEvent.getUrl()).isEqualTo(messagingServiceConnectionPluginProperties.getUrl());
        assertThat(connectionDetailsEvent.getName()).isEqualTo(messagingServiceConnectionPluginProperties.getName());
        assertThat(connectionDetailsEvent.getProperties()).hasSize(1);
        assertThat(connectionDetailsEvent.getProperties().get(0).getName())
                .isEqualTo(messagingServiceConnectionPluginProperties.getProperties().get(0).getName());
        assertThat(connectionDetailsEvent.getProperties().get(0).getValue())
                .isEqualTo(messagingServiceConnectionPluginProperties.getProperties().get(0).getValue());

        assertThat(connectionDetailsEvent.getAuthenticationDetails()).hasSize(1);
        AuthenticationDetailsEvent authenticationDetailsEvent = connectionDetailsEvent.getAuthenticationDetails().get(0);
        assertThat(authenticationDetailsEvent.getProtocol()).isEqualTo(authenticationDetailsPluginProperties.getProtocol());
        assertThat(authenticationDetailsEvent.getProperties()).hasSize(1);
        assertThat(authenticationDetailsEvent.getProperties().get(0).getName())
                .isEqualTo(authenticationDetailsPluginProperties.getProperties().get(0).getName());
        assertThat(authenticationDetailsEvent.getProperties().get(0).getValue())
                .isEqualTo(authenticationDetailsPluginProperties.getProperties().get(0).getValue());

        assertThat(authenticationDetailsEvent.getCredentials()).hasSize(1);
        CredentialDetailsEvent credentialDetailsEvent = authenticationDetailsEvent.getCredentials().get(0);
        assertThat(credentialDetailsEvent.getSource()).isEqualTo(credentialDetailsProperties.getSource());
        assertThat(credentialDetailsEvent.getProperties()).hasSize(1);
        assertThat(credentialDetailsEvent.getProperties().get(0).getName())
                .isEqualTo(credentialDetailsProperties.getProperties().get(0).getName());
        assertThat(credentialDetailsEvent.getProperties().get(0).getValue())
                .isEqualTo(credentialDetailsProperties.getProperties().get(0).getValue());

        assertThat(credentialDetailsEvent.getOperations()).hasSize(1);
        AuthenticationOperationDetailsEvent authenticationOperationDetailsEvent = credentialDetailsEvent.getOperations().get(0);
        assertThat(authenticationOperationDetailsEvent.getName()).isEqualTo(authenticationOperationDetailsProperties.getName());
    }

}

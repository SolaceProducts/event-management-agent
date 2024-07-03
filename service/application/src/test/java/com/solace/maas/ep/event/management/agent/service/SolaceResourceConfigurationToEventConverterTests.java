package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.common.model.EventBrokerAuthenticationConfiguration;
import com.solace.maas.ep.common.model.EventBrokerConnectionConfiguration;
import com.solace.maas.ep.common.model.EventBrokerCredentialConfiguration;
import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import jakarta.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class SolaceResourceConfigurationToEventConverterTests {

    private static final String PROTOCOL_SEMP = "semp";
    private static final String BASIC_AUTHENTICATION = "basicAuthentication";
    @Autowired
    private SolaceResourceConfigurationToEventConverter solaceResourceConfigurationToEventConverter;


    @Test
    void testMapToMessagingServiceEvent() {

        EventBrokerResourceConfiguration resourceConfiguration = buildResourceConfiguration();
        EventBrokerConnectionConfiguration connection = buildConnectionConfiguration();
        EventBrokerAuthenticationConfiguration auth = new EventBrokerAuthenticationConfiguration();
        EventBrokerCredentialConfiguration cred = new EventBrokerCredentialConfiguration();
        cred.setUserName("someUserName");
        cred.setPassword("somePass");
        auth.setCredential(cred);
        connection.setAuthentication(auth);
        resourceConfiguration.setConnections(List.of(connection));
        MessagingServiceEvent mappedEvent = solaceResourceConfigurationToEventConverter.mapToMessagingServiceEvent(resourceConfiguration);

        assertThat(mappedEvent.getId()).isEqualTo(resourceConfiguration.getId());
        assertThat(mappedEvent.getName()).isEqualTo(resourceConfiguration.getName());
        assertThat(mappedEvent.getMessagingServiceType()).isEqualTo(resourceConfiguration.getResourceConfigurationType().name());

        List<ConnectionDetailsEvent> connections = mappedEvent.getConnectionDetails();
        assertThat(connections).hasSize(1);
        ConnectionDetailsEvent connectionDetails = connections.get(0);
        assertThat(connectionDetails.getName()).isEqualTo(resourceConfiguration.getName());
        assertThat(connectionDetails.getUrl()).isEqualTo(connection.getUrl());

        assertThat(connectionDetails.getProperties()).containsExactly(EventProperty.builder().name("sempPageSize").value("100").build(),
                EventProperty.builder().name("msgVpn").value(connection.getMsgVpn()).build());

        assertThat(connectionDetails.getAuthenticationDetails()).hasSize(1);
        AuthenticationDetailsEvent authenticationDetailsEvent = connectionDetails.getAuthenticationDetails().get(0);
        assertThat(authenticationDetailsEvent.getProtocol()).isEqualTo(PROTOCOL_SEMP);
        assertThat(authenticationDetailsEvent.getProperties())
                .containsExactlyInAnyOrder(EventProperty.builder().name("type").value(BASIC_AUTHENTICATION).build()

                );

        assertThat(authenticationDetailsEvent.getCredentials()).hasSize(1);
        CredentialDetailsEvent credentialDetailsEvent = authenticationDetailsEvent.getCredentials().get(0);
        assertThat(credentialDetailsEvent.getProperties())
                .containsExactlyInAnyOrder(
                        EventProperty.builder().name("username").value(cred.getUserName()).build(),
                        EventProperty.builder().name("password").value(cred.getPassword()).build()

                );


    }

    @Nested
    class BeanValidations {
        @Test
        void nullInputFailsValidationCheck() {
            NullPointerException thrown = Assert.assertThrows(NullPointerException.class,
                    () -> solaceResourceConfigurationToEventConverter.mapToMessagingServiceEvent(null));
            assertThat(thrown.getMessage()).isEqualTo("eventBrokerResource cannot be null.");
        }


        @Test
        void beanValidationFailsIfBrokerResourceConfigurationIsInvalid() {
            EventBrokerResourceConfiguration invalidResource = buildResourceConfiguration();
            invalidResource.setId(null);
            invalidResource.setName(null);
            invalidResource.setConnections(List.of());
            invalidResource.setResourceConfigurationType(ResourceConfigurationType.SOLACE);

            validateConstraintViolationException(invalidResource, Set.of("mapToMessagingServiceEvent.eventBrokerResource.name: must not be blank",
                    "mapToMessagingServiceEvent.eventBrokerResource.id: must not be blank", "mapToMessagingServiceEvent.eventBrokerResource.connections: must" +
                            " not be empty"));
        }

        @Test
        void beanValidationFailsIfConnectionConfigurationIsInvalid() {
            EventBrokerResourceConfiguration resourceWithInvalidConnection = buildResourceConfiguration();
            EventBrokerConnectionConfiguration connectionConfiguration = new EventBrokerConnectionConfiguration();
            resourceWithInvalidConnection.setConnections(List.of(connectionConfiguration));

            validateConstraintViolationException(resourceWithInvalidConnection, Set.of("mapToMessagingServiceEvent.eventBrokerResource.connections[0].name: " +
                            "must not be blank", "mapToMessagingServiceEvent.eventBrokerResource.connections[0].msgVpn: must not be blank",
                    "mapToMessagingServiceEvent.eventBrokerResource.connections[0].authentication: must not be null", "mapToMessagingServiceEvent" +
                            ".eventBrokerResource.connections[0].url: must not be blank"));

        }

        @Test
        void beanValidationFailsIfCredentialConfigurationIsInvalid() {
            EventBrokerResourceConfiguration resourceWithInvalidCredentials = buildResourceConfiguration();
            EventBrokerConnectionConfiguration connection2 = buildConnectionConfiguration();
            EventBrokerAuthenticationConfiguration auth = new EventBrokerAuthenticationConfiguration();
            EventBrokerCredentialConfiguration cred = new EventBrokerCredentialConfiguration();
            auth.setCredential(cred);
            connection2.setAuthentication(auth);
            resourceWithInvalidCredentials.setConnections(List.of(connection2));


            validateConstraintViolationException(resourceWithInvalidCredentials, Set.of("mapToMessagingServiceEvent.eventBrokerResource.connections[0]" +
                    ".authentication.credential.password: must not be blank", "mapToMessagingServiceEvent.eventBrokerResource.connections[0].authentication" +
                    ".credential.userName: must not be blank"));
        }

    }

    private static EventBrokerConnectionConfiguration buildConnectionConfiguration() {
        EventBrokerConnectionConfiguration connection2 = new EventBrokerConnectionConfiguration();
        connection2.setMsgVpn("someVpn");
        connection2.setUrl("https://localhost:8080/test-messaging-service");
        connection2.setName("someName");
        return connection2;
    }


    private static EventBrokerResourceConfiguration buildResourceConfiguration() {
        EventBrokerResourceConfiguration resourceWithInvalidConnection = new EventBrokerResourceConfiguration();
        resourceWithInvalidConnection.setName("someFakeName");
        resourceWithInvalidConnection.setResourceConfigurationType(ResourceConfigurationType.SOLACE);
        resourceWithInvalidConnection.setId("someSvcId");
        return resourceWithInvalidConnection;
    }


    private void validateConstraintViolationException(EventBrokerResourceConfiguration resource,
                                                      Set<String> expectedViolations) {
        ConstraintViolationException thrown = Assert.assertThrows(ConstraintViolationException.class,
                () -> solaceResourceConfigurationToEventConverter.mapToMessagingServiceEvent(resource));

        assertThat(thrown.getConstraintViolations()
                .stream().map(
                        violation -> violation.getPropertyPath().toString() + ": " + violation.getMessage()
                ).collect(Collectors.toSet()))
                .containsExactlyInAnyOrderElementsOf(expectedViolations);
    }
}

package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.config.MessagingServiceTypeConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationOperationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import com.solace.maas.ep.event.management.agent.repository.messagingservice.MessagingServiceRepository;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.AuthenticationDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialOperationsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialPropertiesEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class MessagingServiceDelegateServiceTests {
    @Autowired
    private MessagingServiceEventToEntityConverter eventToEntityConverter;
    @Autowired
    private MessagingServiceEntityToEventConverter entityToEventConverter;
    @Mock
    private MessagingServiceRepository repository;

    private MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @BeforeEach
    public void setUp() {
        messagingServiceDelegateService =
                new MessagingServiceDelegateServiceImpl(repository, entityToEventConverter, eventToEntityConverter);
    }

    @Test
    public void testAddMessagingService() {
        AuthenticationDetailsEvent authenticationDetailsEvent = AuthenticationDetailsEvent.builder()
                .id(UUID.randomUUID().toString())
                .protocol("SEMP")
                .credentials(List.of(CredentialDetailsEvent.builder()
                        .operations(List.of(AuthenticationOperationDetailsEvent.builder()
                                .name("ALL")
                                .build()))
                        .properties(List.of(
                                EventProperty.builder()
                                        .name("username")
                                        .value("username")
                                        .build(),
                                EventProperty.builder()
                                        .name("password")
                                        .value("password")
                                        .build()))
                        .build()))
                .build();

        ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                .name("connectionDetails")
                .url("localhost:9090")
                .authenticationDetails(List.of(authenticationDetailsEvent))
                .build();

        MessagingServiceEvent messagingServiceEvent = MessagingServiceEvent.builder()
                .messagingServiceType(MessagingServiceType.SOLACE.name())
                .name("service1")
                .connectionDetails(List.of(connectionDetailsEvent))
                .build();

        MessagingServiceEntity messagingServiceEntity = eventToEntityConverter.convert(messagingServiceEvent);
        when(repository.save(any(MessagingServiceEntity.class)))
                .thenReturn(messagingServiceEntity);

        messagingServiceDelegateService.addMessagingService(messagingServiceEvent);

        assertThatNoException();
    }

    @Test
    public void testGetMessagingServiceClient() {
        MessagingServiceClientManager clientManager = mock(MessagingServiceClientManager.class);

        MessagingServiceTypeConfig.getMessagingServiceManagers().put("KAFKA", clientManager);

        byte[] encryptedPassword = "encryptedPassword".getBytes();

        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .name("service1")
                .id("testService")
                .type(MessagingServiceType.KAFKA.name())
                .build();

        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .messagingService(messagingServiceEntity)
                .name("connectionDetails")
                .url("localhost:9090")
                .build();
        messagingServiceEntity.setConnections(List.of(connectionDetailsEntity));

        AuthenticationDetailsEntity authenticationDetailsEntity = AuthenticationDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
                .connections(connectionDetailsEntity)
                .protocol("PLAINTEXT")
                .build();
        connectionDetailsEntity.setAuthentication(List.of(authenticationDetailsEntity));

        CredentialDetailsEntity credentialDetailsEntity = CredentialDetailsEntity.builder()
                .authentication(authenticationDetailsEntity)
                .properties(List.of(
                        CredentialPropertiesEntity.builder()
                                .name("username")
                                .value("username")
                                .build(),
                        CredentialPropertiesEntity.builder()
                                .name("password")
                                .value(ArrayUtils.toString(encryptedPassword))
                                .build()))
                .build();
        authenticationDetailsEntity.setCredentials(List.of(credentialDetailsEntity));

        CredentialOperationsEntity credentialOperationsEntity = CredentialOperationsEntity.builder()
                .credentials(credentialDetailsEntity)
                .name("ALL")
                .build();
        credentialDetailsEntity.setOperations(List.of(credentialOperationsEntity));

        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(messagingServiceEntity));

        when(clientManager.getClient(any(ConnectionDetailsEvent.class)))
                .thenReturn(mock(AdminClient.class));

        AdminClient result = messagingServiceDelegateService.getMessagingServiceClient("testService");

        result.close();

        assertThat(result).isNotNull();
    }

}

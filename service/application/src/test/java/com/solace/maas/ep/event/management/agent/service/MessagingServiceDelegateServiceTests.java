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
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import org.apache.kafka.clients.admin.AdminClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private final MessagingServiceEventToEntityConverter messagingServiceEventToEntityConverter =
            new MessagingServiceEventToEntityConverter();
    @Mock
    private MessagingServiceRepository repository;

    @InjectMocks
    private MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

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

        MessagingServiceEntity messagingServiceEntity = messagingServiceEventToEntityConverter.convert(messagingServiceEvent);
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

        AuthenticationDetailsEntity authenticationDetailsEntity = AuthenticationDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
//                .username("username")
//                .password(ArrayUtils.toObject(encryptedPassword))
                .build();

        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("testConnectionName")
//                .connectionUrl("localhost:9090")
//                .authenticationDetails(List.of(authenticationDetailsEntity))
                .build();

        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(MessagingServiceEntity.builder()
//                        .messagingServiceType(MessagingServiceType.KAFKA.name())
                        .name("service1")
                        .id(UUID.randomUUID().toString())
//                        .managementDetails(List.of(connectionDetailsEntity))
                        .build()));
        when(clientManager.getClient(any(ConnectionDetailsEvent.class)))
                .thenReturn(mock(AdminClient.class));

        AdminClient result = messagingServiceDelegateService.getMessagingServiceClient("testService");

        result.close();

        assertThat(result).isNotNull();
    }

}

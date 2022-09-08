package com.solace.maas.ep.runtime.agent.service;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.runtime.agent.repository.messagingservice.MessagingServiceRepository;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.AuthenticationDetailsEntity;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.semp.SolaceHttpSemp;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class SolaceMessagingServiceDelegateServiceTests {
    @Mock
    private MessagingServiceRepository repository;

    @Mock
    private Map<String, MessagingServiceClientManager<?>> messagingServiceManagers;

    @InjectMocks
    private MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @Test
    public void testSolaceGetMessagingServiceClient() {
        MessagingServiceClientManager clientManager = mock(MessagingServiceClientManager.class);

        byte[] encryptedPassword = "encryptedPassword".getBytes();

        AuthenticationDetailsEntity authenticationDetailsEntity = AuthenticationDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
                .username("username")
                .password(ArrayUtils.toObject(encryptedPassword))
                .build();

        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("testConnectionName")
                .connectionUrl("connectionUrl")
                .msgVpn("msgVPN")
                .authenticationDetails(List.of(authenticationDetailsEntity))
                .build();

        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(MessagingServiceEntity.builder()
                        .messagingServiceType(MessagingServiceType.SOLACE.name())
                        .name("service1")
                        .id(UUID.randomUUID().toString())
                        .managementDetails(List.of(connectionDetailsEntity))
                        .build()));
        when(messagingServiceManagers.containsKey(any(String.class)))
                .thenReturn(true);
        when(messagingServiceManagers.get(any(String.class)))
                .thenReturn(clientManager);
        when(clientManager.getClient(any(ConnectionDetailsEvent.class)))
                .thenReturn(mock(SolaceHttpSemp.class));

        SolaceHttpSemp result = messagingServiceDelegateService
                .getMessagingServiceClient("test service");

        assertThat(result).isNotNull();
    }
}

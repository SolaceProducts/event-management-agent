package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.repository.messagingservice.ConnectionDetailsRepository;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ConnectionDetailsServiceTests {
    @Mock
    private ConnectionDetailsRepository repository;

    @InjectMocks
    private ConnectionDetailsService connectionDetailsService;

    @Test
    public void testSave() {
        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .connectionUrl("localhost:9090")
                .id(UUID.randomUUID().toString())
                .build();

        when(repository.save(any(ConnectionDetailsEntity.class)))
                .thenReturn(connectionDetailsEntity);

        ConnectionDetailsEntity result = connectionDetailsService.save(connectionDetailsEntity);

        assertThat(result.getUrl())
                .isEqualTo(connectionDetailsEntity.getUrl());
    }

    @Test
    public void testFindByMessagingServiceId() {
        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .connectionUrl("localhost:9090")
                .id(UUID.randomUUID().toString())
                .build();

        when(repository.findConnectionDetailsEntityByMessagingServiceId("testService"))
                .thenReturn(Optional.of(connectionDetailsEntity));

        Optional<ConnectionDetailsEntity> result = connectionDetailsService.findByMessagingServiceId("testService");

        assertThat(result)
                .isNotEmpty();
    }
}

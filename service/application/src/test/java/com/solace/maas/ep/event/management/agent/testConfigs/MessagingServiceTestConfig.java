package com.solace.maas.ep.event.management.agent.testConfigs;

import com.solace.maas.ep.event.management.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.event.management.agent.repository.messagingservice.MessagingServiceRepository;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceEntityToEventConverter;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceEventToEntityConverter;
import com.solace.messaging.DirectMessageReceiverBuilder;
import com.solace.messaging.MessagingService;
import com.solace.messaging.PersistentMessageReceiverBuilder;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@Profile("TEST")
@Slf4j
public class MessagingServiceTestConfig {
    @Bean
    @Primary
    public MessagingServiceRepository getRepository() {
        MessagingServiceRepository repository = mock(MessagingServiceRepository.class);
        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
                .url("localhost:9090")
                .build();

        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(MessagingServiceEntity.builder()
                        .type(MessagingServiceType.SOLACE.name())
                        .name("service1")
                        .id(UUID.randomUUID().toString())
                        .connections(List.of(connectionDetailsEntity))
                        .build()));
        return repository;
    }

    @Bean
    @Primary
    public MessagingServiceEntityToEventConverter entityToEventConverter() {
        return new MessagingServiceEntityToEventConverter();
    }

    @Bean
    @Primary
    public MessagingService messagingService() {
        MessagingService mockedSvc = mock(MessagingService.class);
        PersistentMessageReceiverBuilder persistentMessageReceiverBuilder = mock(PersistentMessageReceiverBuilder.class);
        PersistentMessageReceiver mockedPersistentMessageReceiver = mock(PersistentMessageReceiver.class);

        when(persistentMessageReceiverBuilder.build(any())).thenReturn(mockedPersistentMessageReceiver);
        when(mockedPersistentMessageReceiver.start()).thenReturn(mockedPersistentMessageReceiver);
        when(mockedSvc.createPersistentMessageReceiverBuilder())
                .thenReturn(persistentMessageReceiverBuilder);


        DirectMessageReceiverBuilder directMessageReceiverBuilder = mock(DirectMessageReceiverBuilder.class);
        DirectMessageReceiver directMessageReceiver = mock(DirectMessageReceiver.class);
        when(directMessageReceiverBuilder.withSubscriptions(any())).thenReturn(directMessageReceiverBuilder);
        when(directMessageReceiverBuilder.withSubscriptions(any()).build()).thenReturn(directMessageReceiver);
        when(directMessageReceiver.start()).thenReturn(directMessageReceiver);
        when(mockedSvc.createDirectMessageReceiverBuilder())
                .thenReturn(directMessageReceiverBuilder);

        doNothing().when(directMessageReceiver).receiveAsync(any());
        return mockedSvc;
    }

    @Bean
    @Primary
    public MessagingServiceEventToEntityConverter eventToEntityConverter() {
        return new MessagingServiceEventToEntityConverter();
    }
}

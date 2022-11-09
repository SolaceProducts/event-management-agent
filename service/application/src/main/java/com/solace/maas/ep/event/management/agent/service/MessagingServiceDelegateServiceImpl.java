package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.config.MessagingServiceTypeConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.repository.messagingservice.MessagingServiceRepository;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manages the creation and retrieval of Messaging Service information.
 */
@Slf4j
@Service
public class MessagingServiceDelegateServiceImpl implements MessagingServiceDelegateService {
    private final MessagingServiceRepository repository;
    private final MessagingServiceEntityToEventConverter entityToEventConverter;
    private final MessagingServiceEventToEntityConverter eventToEntityConverter;
    private final Map<String, Object> messagingServiceClients;


    @Autowired
    public MessagingServiceDelegateServiceImpl(MessagingServiceRepository repository,
                                               MessagingServiceEntityToEventConverter entityToEventConverter,
                                               MessagingServiceEventToEntityConverter eventToEntityConverter) {
        this.repository = repository;
        this.entityToEventConverter = entityToEventConverter;
        this.eventToEntityConverter = eventToEntityConverter;
        messagingServiceClients = new HashMap<>();
    }

    /**
     * Adds a Messaging Service. Right now the only information we're storing is the Connection Details for
     * a Messaging Service. Later on there will be more information.
     *
     * @param messagingServiceEvent Messaging Service Details for a Messaging Service.
     */
    @Transactional
    public MessagingServiceEntity addMessagingService(MessagingServiceEvent messagingServiceEvent) {
        MessagingServiceEntity messagingServiceEntity = eventToEntityConverter.convert(messagingServiceEvent);

        return repository.save(messagingServiceEntity);
    }

    @Transactional
    public Iterable<MessagingServiceEntity> addMessagingServices(List<MessagingServiceEvent> messagingServiceEvents) {
        List<MessagingServiceEntity> messagingServiceEntities = messagingServiceEvents.stream()
                .map(eventToEntityConverter::convert)
                .collect(Collectors.toUnmodifiableList());

        return repository.saveAll(messagingServiceEntities);
    }

    /**
     * Retrieves a specific Messaging Service by ID.
     *
     * @param messagingServiceId The Messaging Service ID.
     * @return The retrieved Messaging Service.
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public MessagingServiceEntity getMessagingServiceById(String messagingServiceId) {
        Optional<MessagingServiceEntity> messagingServiceEntityOpt = repository.findById(messagingServiceId);
        return messagingServiceEntityOpt.orElseThrow(() -> {
            String message = String.format("Could not find messaging service with id [%s].", messagingServiceId);
            log.error(message);
            return new NoSuchElementException(message);
        });
    }

    /**
     * Retrieves a Client Connection for a specific Messaging Service. This will use the stored Connection Details
     * to create the Client Connection.
     *
     * @param messagingServiceId The Messaging Service ID.
     * @param <T>                The Type of the Connection Client being created.
     * @return The created Connection Client.
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public <T> T getMessagingServiceClient(String messagingServiceId) {
        log.trace("Retrieving connection details for messaging service {}.", messagingServiceId);

        MessagingServiceEntity messagingServiceEntity = getMessagingServiceById(messagingServiceId);

        // Get the Messaging Service type.
        String type = messagingServiceEntity.getType();

        if (messagingServiceClients.containsKey(messagingServiceId)) {
            return (T) messagingServiceClients.get(messagingServiceId);
        } else if (MessagingServiceTypeConfig.getMessagingServiceManagers().containsKey(type)) {
            // Attempt to retrieve the Messaging Service Manager for this type of Messaging Service. If it is found,
            // we will attempt to create a Connection Client.
            MessagingServiceClientManager<?> clientManager =
                    MessagingServiceTypeConfig.getMessagingServiceManagers().get(type);

            MessagingServiceEvent messagingServiceEvent = entityToEventConverter.convert(messagingServiceEntity);
            ConnectionDetailsEvent connectionDetailsEvent = messagingServiceEvent.getConnectionDetails().stream()
                    .findFirst()
                    .orElseThrow(() -> {
                        String message = String.format("Could not find connection details for [%s] messaging service with name: [%s], id: [%s].",
                                messagingServiceEntity.getType(),
                                messagingServiceEntity.getName(),
                                messagingServiceId);
                        log.error(message);
                        return new NoSuchElementException(message);
                    });

            T messagingServiceClient = (T) clientManager.getClient(connectionDetailsEvent);
            messagingServiceClients.put(messagingServiceId, messagingServiceClient);

            return messagingServiceClient;
        } else {
            String message = String.format("Could not retrieve or create the messaging service client for [%s].", messagingServiceId);
            log.error(message);
            throw new RuntimeException(message);
        }
    }
}

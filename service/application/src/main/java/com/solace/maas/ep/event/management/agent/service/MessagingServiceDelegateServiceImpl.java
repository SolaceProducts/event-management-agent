package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.config.MessagingServicePluginProperties;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.config.MessagingServiceTypeConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.repository.messagingservice.MessagingServiceRepository;
import com.solace.maas.ep.event.management.agent.repository.messagingservice.ServiceAssociationsRepository;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ServiceAssociationsCompositeKey;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ServiceAssociationsEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

/**
 * Manages the creation and retrieval of Messaging Service information.
 */
@Slf4j
@Service
public class MessagingServiceDelegateServiceImpl implements MessagingServiceDelegateService {
    private final MessagingServiceRepository repository;
    private final ServiceAssociationsRepository serviceAssociationsRepository;
    private final MessagingServiceEntityToEventConverter entityToEventConverter;
    private final MessagingServiceEventToEntityConverter eventToEntityConverter;
    private final Map<String, Object> messagingServiceClients;


    @Autowired
    public MessagingServiceDelegateServiceImpl(MessagingServiceRepository repository,
                                               MessagingServiceEntityToEventConverter entityToEventConverter,
                                               MessagingServiceEventToEntityConverter eventToEntityConverter,
                                               ServiceAssociationsRepository serviceAssociationsRepository) {
        this.repository = repository;
        this.entityToEventConverter = entityToEventConverter;
        this.eventToEntityConverter = eventToEntityConverter;
        this.serviceAssociationsRepository = serviceAssociationsRepository;
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


    @Transactional
    public Iterable<MessagingServiceEntity> upsertMessagingServiceEvents(List<MessagingServiceEvent> messagingServiceEvents) {
        if (CollectionUtils.isEmpty(messagingServiceEvents)) {
            return List.of();
        }
        List<MessagingServiceEntity> messagingServiceEntities = messagingServiceEvents.stream()
                .map(toBeUpserted -> {
                    MessagingServiceEntity updated = eventToEntityConverter.convert(toBeUpserted);
                    Optional<MessagingServiceEntity> existing = repository.findById(toBeUpserted.getId());
                    if (existing.isPresent()) {
                        MessagingServiceEntity existingEntity = existing.get();
                        updated.setScanEntities(existingEntity.getScanEntities());
                    }
                    return updated;

                }).collect(toCollection(ArrayList::new));
        return repository.saveAll(messagingServiceEntities);
    }

    /**
     * Retrieves a specific Messaging Service by ID.
     *
     * @param messagingServiceId The Messaging Service ID.
     * @return The retrieved Messaging Service.
     */
    @SuppressWarnings("unchecked")
    @Override
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

    @Transactional
    public void addMessagingServicesRelations(List<MessagingServicePluginProperties> messagingServices) {
        List<ServiceAssociationsEntity> serviceAssociationsEntityList = new ArrayList<>();
        for (MessagingServicePluginProperties messagingService : messagingServices) {
            if (!ObjectUtils.isEmpty(messagingService.getRelatedServices())) {
                messagingService.getRelatedServices().forEach(relatedService ->
                        serviceAssociationsEntityList.add(
                                ServiceAssociationsEntity.builder()
                                        .serviceAssociationsId(ServiceAssociationsCompositeKey.builder()
                                                .parent(getMessagingServiceById(messagingService.getId()))
                                                .child(getMessagingServiceById(relatedService))
                                                .build())
                                        .build()));
            }
        }
        serviceAssociationsRepository.saveAll(serviceAssociationsEntityList);
    }

    public Set<MessagingServiceEntity> getMessagingServicesRelations(String messagingServiceId) {
        Set<MessagingServiceEntity> messagingServiceEntitySet = new HashSet<>();

        List<ServiceAssociationsEntity> serviceAssociationsEntityListParent =
                serviceAssociationsRepository.findByServiceAssociationsId_Parent_Id(messagingServiceId);
        serviceAssociationsEntityListParent.forEach(serviceAssociationsEntity ->
                messagingServiceEntitySet.add(serviceAssociationsEntity.getServiceAssociationsId().getChild()));

        List<ServiceAssociationsEntity> serviceAssociationsEntityListChild =
                serviceAssociationsRepository.findByServiceAssociationsId_Child_Id(messagingServiceId);
        serviceAssociationsEntityListChild.forEach(serviceAssociationsEntity ->
                messagingServiceEntitySet.add(serviceAssociationsEntity.getServiceAssociationsId().getParent()));

        return messagingServiceEntitySet;
    }
}

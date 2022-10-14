package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.messagingservice.ConnectionDetailsRepository;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ConnectionDetailsService manages the connection information required to Connect to a Messaging Service.
 */
@Service
public class ConnectionDetailsService {
    private final ConnectionDetailsRepository repository;

    @Autowired
    public ConnectionDetailsService(ConnectionDetailsRepository repository) {
        this.repository = repository;
    }

    /**
     * Just saving the Connection Details for now. Later on this method will do more.
     *
     * @param connectionDetailsEntity The Connection Details to save.
     * @return The saved Connection Details.
     */
    public ConnectionDetailsEntity save(ConnectionDetailsEntity connectionDetailsEntity) {
        return repository.save(connectionDetailsEntity);
    }

    /**
     * Find Connection Details by a Messaging Service ID.
     *
     * @param messagingServiceId The ID of the Messaging Service to find Connection Details for.
     * @return An Optional result containing Connection Details.
     */
    public Optional<ConnectionDetailsEntity> findByMessagingServiceId(String messagingServiceId) {
        return repository.findConnectionDetailsEntityByMessagingServiceId(messagingServiceId);
    }
}

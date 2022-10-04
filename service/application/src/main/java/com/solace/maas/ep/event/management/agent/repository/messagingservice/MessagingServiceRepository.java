package com.solace.maas.ep.event.management.agent.repository.messagingservice;

import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagingServiceRepository extends CrudRepository<MessagingServiceEntity, String> {
}

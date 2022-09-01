package com.solace.maas.ep.runtime.agent.repository.messagingservice;

import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.MessagingServiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagingServiceRepository extends CrudRepository<MessagingServiceEntity, String> {
}

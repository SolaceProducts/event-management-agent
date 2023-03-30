package com.solace.maas.ep.event.management.agent.repository.messagingservice;

import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ServiceAssociationsCompositeKey;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ServiceAssociationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAssociationsRepository extends JpaRepository<ServiceAssociationsEntity, ServiceAssociationsCompositeKey> {
    List<ServiceAssociationsEntity> findByServiceAssociationsId_Parent_Id(String messagingServiceId);

    List<ServiceAssociationsEntity> findByServiceAssociationsId_Child_Id(String messagingServiceId);
}

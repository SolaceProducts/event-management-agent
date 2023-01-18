package com.solace.maas.ep.event.management.agent.repository.scan;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanRepository extends CrudRepository<ScanEntity, String> {
    Page<ScanEntity> findAll(Pageable pageable);

    Page<ScanEntity> findAllByMessagingServiceId(String messagingServiceId, Pageable pageable);
}

package com.solace.maas.ep.event.management.agent.repository.scan;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanRepository extends CrudRepository<ScanEntity, String> {
}

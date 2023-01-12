package com.solace.maas.ep.event.management.agent.repository.scan;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import org.springframework.data.repository.CrudRepository;

public interface ScanStatusRepository extends CrudRepository<ScanStatusEntity, String> {
}

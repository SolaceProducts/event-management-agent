package com.solace.maas.ep.event.management.agent.repository.scan;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScanStatusRepository extends CrudRepository<ScanStatusEntity, String> {

    ScanStatusEntity findByScanType(ScanTypeEntity scanType);
}

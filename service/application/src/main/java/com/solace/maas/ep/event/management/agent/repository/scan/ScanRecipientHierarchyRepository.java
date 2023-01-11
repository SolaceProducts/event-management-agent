package com.solace.maas.ep.event.management.agent.repository.scan;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientHierarchyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanRecipientHierarchyRepository extends CrudRepository<ScanRecipientHierarchyEntity, String> {
    List<ScanRecipientHierarchyEntity> findScanRecipientHierarchyEntitiesByScanId(String scanId);
}

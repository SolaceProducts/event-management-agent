package com.solace.maas.ep.event.management.agent.repository.scan;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientsPathEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanRecipientStoreRepository extends CrudRepository<ScanRecipientsPathEntity, String> {
    List<ScanRecipientsPathEntity> findScanRecipientPathEntitiesByScanId(String scanId);
}

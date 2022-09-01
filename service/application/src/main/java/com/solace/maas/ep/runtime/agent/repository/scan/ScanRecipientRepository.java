package com.solace.maas.ep.runtime.agent.repository.scan;

import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanRecipientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanRecipientRepository extends CrudRepository<ScanRecipientEntity, String> {
    List<ScanRecipientEntity> findAllByScanIdAndRouteId(String scanId, String parentRoute);
}

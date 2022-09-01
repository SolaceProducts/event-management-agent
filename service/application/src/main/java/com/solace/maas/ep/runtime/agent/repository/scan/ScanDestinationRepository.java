package com.solace.maas.ep.runtime.agent.repository.scan;

import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanDestinationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanDestinationRepository extends CrudRepository<ScanDestinationEntity, String> {
    List<ScanDestinationEntity> findAllByScanIdAndRouteId(String scanId, String routeId);
}

package com.solace.maas.ep.event.management.agent.repository.scan;

import com.solace.maas.ep.event.management.agent.repository.model.scan.AsyncScanProcessEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsyncScanProcessRepository extends CrudRepository<AsyncScanProcessEntity, String> {
    Optional<AsyncScanProcessEntity> findAsyncScanProcessEntityByScanIdAndScanType(String scanId, String scanType);
}

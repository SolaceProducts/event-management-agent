package com.solace.maas.ep.event.management.agent.repository.manualimport;

import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportDetailsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManualImportDetailsRepository extends CrudRepository<ManualImportDetailsEntity, String> {
    Optional<ManualImportDetailsEntity> getByScanId(String scanId);
}

package com.solace.maas.ep.event.management.agent.repository.manualimport;

import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportFilesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualImportFilesRepository extends CrudRepository<ManualImportFilesEntity, String> {
    List<ManualImportFilesEntity> getAllByScanId(String scanId);
}

package com.solace.maas.ep.event.management.agent.repository.manualimport;

import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManualImportRepository extends CrudRepository<ManualImportEntity, String> {
}

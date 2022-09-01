package com.solace.maas.ep.runtime.agent.repository.file;

import com.solace.maas.ep.runtime.agent.repository.model.file.DataCollectionFileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataCollectionFileRepository extends CrudRepository<DataCollectionFileEntity, String> {
    Optional<DataCollectionFileEntity> findByPath(String path);

    List<DataCollectionFileEntity> findDataCollectionFileEntitiesByScanId(String scanId);
}

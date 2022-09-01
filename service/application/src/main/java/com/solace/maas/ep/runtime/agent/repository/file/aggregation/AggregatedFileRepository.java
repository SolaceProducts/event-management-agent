package com.solace.maas.ep.runtime.agent.repository.file.aggregation;

import com.solace.maas.ep.runtime.agent.repository.model.file.aggregation.AggregatedFileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AggregatedFileRepository extends CrudRepository<AggregatedFileEntity, String> {
    Optional<AggregatedFileEntity> findByPath(String path);
}

package com.solace.maas.ep.event.management.agent.scanManager.mapper;

import com.solace.maas.ep.event.management.agent.scanManager.model.DataCollectionFileBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.DataCollectionFileDTO;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface DataCollectionFileMapper {
    DataCollectionFileBO map(DataCollectionFileDTO dto);

    DataCollectionFileDTO map(DataCollectionFileBO bo);
}

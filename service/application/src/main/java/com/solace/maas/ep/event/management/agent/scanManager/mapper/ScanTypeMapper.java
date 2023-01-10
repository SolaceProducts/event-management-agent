package com.solace.maas.ep.event.management.agent.scanManager.mapper;

import com.solace.maas.ep.event.management.agent.scanManager.model.ScanTypeBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanTypeDTO;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ScanTypeMapper {
    ScanTypeBO map(ScanTypeDTO dto);

    ScanTypeDTO map(ScanTypeBO bo);
}

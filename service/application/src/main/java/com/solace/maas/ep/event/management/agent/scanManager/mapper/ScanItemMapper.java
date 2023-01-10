package com.solace.maas.ep.event.management.agent.scanManager.mapper;

import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemDTO;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ScanItemMapper {
    ScanItemBO map(ScanItemDTO dto);

    ScanItemDTO map(ScanItemBO bo);
}

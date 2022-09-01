package com.solace.maas.ep.runtime.agent.scanManager.mapper;

import com.solace.maas.ep.runtime.agent.mapper.DtoMapper;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.common.model.ScanRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public abstract class ScanRequestMapper implements DtoMapper<ScanRequestBO, ScanRequestDTO> {

    @Override
    @ValueMapping(target = "scanType", source = "scanType")
    @Mappings(value = {
            @Mapping(target = "entityTypes", source = "entityTypes"),
            @Mapping(target = "destinations", source = "destinations")
    })
    public abstract ScanRequestDTO map(ScanRequestBO source);

    @Override
    @ValueMapping(target = "scanType", source = "scanType")
    @Mappings(value = {
            @Mapping(target = "entityTypes", source = "entityTypes"),
            @Mapping(target = "destinations", source = "destinations")
    })
    public abstract ScanRequestBO map(ScanRequestDTO source);
}

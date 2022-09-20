package com.solace.maas.ep.runtime.agent.scanManager.mapper;

import com.solace.maas.ep.runtime.agent.mapper.DtoMapper;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.common.model.ScanRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class ScanRequestMapper implements DtoMapper<ScanRequestBO, ScanRequestDTO> {

    @Override
    @Mappings(value = {
            @Mapping(target = "scanTypes", source = "scanTypes"),
            @Mapping(target = "destinations", source = "destinations")
    })
    public abstract ScanRequestDTO map(ScanRequestBO source);

    @Override
    @Mappings(value = {
            @Mapping(target = "scanTypes", source = "scanTypes"),
            @Mapping(target = "destinations", source = "destinations")
    })
    public abstract ScanRequestBO map(ScanRequestDTO source);
}

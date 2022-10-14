package com.solace.maas.ep.event.management.agent.mapper;

import com.solace.maas.ep.event.management.agent.model.BaseBO;
import com.solace.maas.ep.event.management.agent.model.User;

public interface DtoMapper<BO extends BaseBO<?>, DTO> {

    DTO map(BO input);

    BO map(DTO input);

    DTO map(BO input, User user);
}

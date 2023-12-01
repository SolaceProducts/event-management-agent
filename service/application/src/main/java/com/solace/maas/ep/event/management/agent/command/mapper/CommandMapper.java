package com.solace.maas.ep.event.management.agent.command.mapper;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {

    CommandRequest map(CommandMessage input);

    CommandMessage map(CommandRequest input);
}
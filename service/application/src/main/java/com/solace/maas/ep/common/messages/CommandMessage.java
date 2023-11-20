package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

import java.util.List;

@Data
public class CommandMessage extends MOPMessage {

    private String correlationId;
    private String context;
    private String messagingServiceId;
    private List<CommandBundle> commandBundles;

    public CommandMessage() {
        super();
    }

    public CommandMessage(String messagingServiceId,
                          String correlationId,
                          String context,
                          List<CommandBundle> commandBundles) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.commandProtocol)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.messagingServiceId = messagingServiceId;
        this.correlationId = correlationId;
        this.context = context;
        this.commandBundles = commandBundles;
    }

    @Override
    public String toLog() {
        return null;
    }
}

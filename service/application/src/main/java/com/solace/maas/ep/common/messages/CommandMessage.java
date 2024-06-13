package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

import java.util.List;

@Data
public class CommandMessage extends MOPMessage {

    private String commandCorrelationId;
    private String context;
    private String serviceId;
    private JobStatus status;
    private List<CommandBundle> commandBundles;

    public CommandMessage() {
        super();
    }

    public CommandMessage(String serviceId,
                          String commandCorrelationId,
                          String context,
                          JobStatus status,
                          List<CommandBundle> commandBundles) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.epConfigPush)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.serviceId = serviceId;
        this.commandCorrelationId = commandCorrelationId;
        this.context = context;
        this.status = status;
        this.commandBundles = commandBundles;
    }

    @Override
    public String toLog() {
        return null;
    }
}

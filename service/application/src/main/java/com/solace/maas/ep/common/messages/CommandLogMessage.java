package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class CommandLogMessage extends MOPMessage {
    String orgId;

    String commandCorrelationId;

    String level;

    String log;

    Long timestamp;

    public CommandLogMessage(String orgId, String commandCorrelationId, String traceId, String actorId, String level, String log, Long timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.epConfigPush)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.commandCorrelationId = commandCorrelationId;
        this.level = level;
        this.log = log;
        this.timestamp = timestamp;
        setTraceId(traceId);
        setActorId(actorId);
    }

    @Override
    public String toLog() {
        return null;
    }
}

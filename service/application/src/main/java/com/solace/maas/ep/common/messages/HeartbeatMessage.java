package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeartbeatMessage extends MOPMessage {

    private String orgId;
    private String runtimeAgentId;
    private String timestamp;
    private String runtimeAgentVersion;


    public HeartbeatMessage(String runtimeAgentId,
                            String timestamp,
                            String runtimeAgentVersion) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.EMAHeartbeat)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.runtimeAgentId = runtimeAgentId;
        this.timestamp = timestamp;
        this.runtimeAgentVersion = runtimeAgentVersion;
    }

    @Override
    public String toLog() {
        return null;
    }
}

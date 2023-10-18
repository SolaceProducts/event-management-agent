package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class ScanDataMessage extends MOPMessage {
    String orgId;

    String scanId;

    String scanType;

    String data;

    private String timestamp;

    public ScanDataMessage(String orgId, String scanId, String traceId, String actorId, String scanType, String data, String timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanData)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.scanType = scanType;
        this.data = data;
        this.timestamp = timestamp;
        setTraceId(traceId);
        setActorId(actorId);
    }

    @Override
    public String toLog() {
        return null;
    }
}

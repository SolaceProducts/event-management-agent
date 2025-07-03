package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class ScanDataMessage extends MOPMessage {
    private String orgId;

    private String originOrgId;

    private String scanId;

    private String scanType;

    private String data;

    private String timestamp;

    public ScanDataMessage(String orgId,
                           String originOrgId,
                           String scanId,
                           String traceId,
                           String actorId,
                           String scanType,
                           String data,
                           String timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanData)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.originOrgId = originOrgId;
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

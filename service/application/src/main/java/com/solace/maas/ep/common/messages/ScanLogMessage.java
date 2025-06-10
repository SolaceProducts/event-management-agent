package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class ScanLogMessage extends MOPMessage {
    private String orgId;

    private String scanId;

    private String level;

    private String log;

    private Long timestamp;

    private String originOrgId;

    public ScanLogMessage(String orgId, String originOrgId, String scanId, String traceId, String actorId, String level, String log, Long timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanDataControl)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.originOrgId = originOrgId;
        this.scanId = scanId;
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

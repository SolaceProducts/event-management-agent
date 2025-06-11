package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScanDataStatusMessage extends MOPMessage {
    // Status per data collection type.

    private String orgId;

    private String scanId;

    private String status;

    private String description;

    private String scanType;
    private String originOrgId;

    public ScanDataStatusMessage(String orgId,
                                 String originOrgId,
                                 String scanId,
                                 String traceId,
                                 String actorId,
                                 String status,
                                 String description,
                                 String scanType) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanDataControl)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.originOrgId = originOrgId;
        this.scanId = scanId;
        this.status = status;
        this.description = description;
        this.scanType = scanType;
        setTraceId(traceId);
        setActorId(actorId);
    }

    @Override
    public String toLog() {
        return null;
    }
}

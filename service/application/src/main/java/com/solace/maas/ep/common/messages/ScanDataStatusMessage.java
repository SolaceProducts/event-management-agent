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

    String orgId;

    String scanId;

    String status;

    String description;

    String scanType;

    public ScanDataStatusMessage(String orgId, String scanId, String status, String description, String scanType) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.status = status;
        this.description = description;
        this.scanType = scanType;
    }

    @Override
    public String toLog() {
        return null;
    }
}

package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScanStatusMessage extends MOPMessage {
    // Status of the overall scan.

    String orgId;

    String scanId;

    String traceId;

    String status;

    String description;

    private List<String> scanTypes;

    public ScanStatusMessage(String orgId, String scanId, String traceId, String status, String description, List<String> scanTypes) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanDataControl)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.traceId = traceId;
        this.status = status;
        this.description = description;
        this.scanTypes = scanTypes;
    }

    @Override
    public String toLog() {
        return null;
    }
}

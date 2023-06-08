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
public class ScanDataImportMessage extends MOPMessage {
    String orgId;

    String scanId;

    String messagingServiceId;

    String runtimeAgentId;

    private List<String> scanTypes;

    public ScanDataImportMessage() {
        super();
    }

    public ScanDataImportMessage(String orgId, String scanId, String messagingServiceId, List<String> scanTypes, String runtimeAgentId) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanDataControl)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.messagingServiceId = messagingServiceId;
        this.scanTypes = scanTypes;
        this.runtimeAgentId = runtimeAgentId;
    }
}

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

    String dataCollectionType;

    String data;

    private String timestamp;

    public ScanDataMessage(String orgId, String scanId, String dataCollectionType, String data, String timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.dataCollectionType = dataCollectionType;
        this.data = data;
        this.timestamp = timestamp;
    }
}

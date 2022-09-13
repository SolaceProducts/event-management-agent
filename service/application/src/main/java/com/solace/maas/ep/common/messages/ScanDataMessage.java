package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class ScanDataMessage extends MOPMessage {
    String messagingServiceId;

    String scanId;

    String dataCollectionType;

    String data;

    private String timestamp;

    public ScanDataMessage(String messagingServiceId, String scanId, String dataCollectionType, String data, String timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.messagingServiceId = messagingServiceId;
        this.scanId = scanId;
        this.dataCollectionType = dataCollectionType;
        this.data = data;
        this.timestamp = timestamp;
    }
}

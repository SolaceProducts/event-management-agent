package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.common.model.ScanRequestDTO;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class ScanCommandMessage extends MOPMessage {

    private ScanRequestDTO scanRequest;
    private String messagingServiceId;

    public ScanCommandMessage() {
        super();
    }

    public ScanCommandMessage(String messagingServiceId, ScanRequestDTO scanRequest) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.messagingServiceId = messagingServiceId;
        this.scanRequest = scanRequest;
    }
}

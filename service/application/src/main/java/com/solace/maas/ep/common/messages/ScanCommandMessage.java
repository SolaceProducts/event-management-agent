package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.common.model.ScanDestination;
import com.solace.maas.ep.common.model.ScanType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

import java.util.List;

@Data
public class ScanCommandMessage extends MOPMessage {

    private String messagingServiceId;
    private String scanId;
    private List<ScanType> scanTypes;
    private List<ScanDestination> destinations;

    public ScanCommandMessage() {
        super();
    }

    public ScanCommandMessage(String messagingServiceId,
                              String scanId,
                              List<ScanType> scanTypes,
                              List<ScanDestination> destinations) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.messagingServiceId = messagingServiceId;
        this.scanId = scanId;
        this.scanTypes = scanTypes;
        this.destinations = destinations;
    }
}

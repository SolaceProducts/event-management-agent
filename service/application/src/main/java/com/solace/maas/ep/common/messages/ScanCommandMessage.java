package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.common.model.CommandMessageWithResources;
import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ScanDestination;
import com.solace.maas.ep.common.model.ScanType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

import java.util.List;

@Data
public class ScanCommandMessage extends MOPMessage implements CommandMessageWithResources {

    private String messagingServiceId;
    private String scanId;
    private List<ScanType> scanTypes;
    private List<ScanDestination> destinations;
    private List<EventBrokerResourceConfiguration> resources;

    public ScanCommandMessage() {
        super();
    }

    public ScanCommandMessage(String messagingServiceId,
                              String scanId,
                              List<ScanType> scanTypes,
                              List<ScanDestination> destinations,
                              List<EventBrokerResourceConfiguration> resources) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanDataControl)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.messagingServiceId = messagingServiceId;
        this.scanId = scanId;
        this.scanTypes = scanTypes;
        this.destinations = destinations;
        this.resources = resources;
    }

    public ScanCommandMessage(String messagingServiceId,
                              String scanId,
                              List<ScanType> scanTypes,
                              List<ScanDestination> destinations) {
        this(messagingServiceId, scanId, scanTypes, destinations, null);
    }



    @Override
    public String toLog() {
        return null;
    }
}

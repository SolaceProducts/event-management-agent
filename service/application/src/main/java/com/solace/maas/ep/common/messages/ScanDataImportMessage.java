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

    private List<String> scanTypes;

    private String emaId;

    private String scheduleId;

    public ScanDataImportMessage() {
        super();
    }

    public ScanDataImportMessage(String orgId, String scanId, String messagingServiceId, List<String> scanTypes,
                                 String emaId, String scheduleId) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.messagingServiceId = messagingServiceId;
        this.scanTypes = scanTypes;
        this.emaId = emaId;
        this.scheduleId = scheduleId;
    }
}

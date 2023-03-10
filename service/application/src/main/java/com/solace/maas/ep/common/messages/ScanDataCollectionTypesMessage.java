package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.common.model.ScanType;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ExcludeFromJacocoGeneratedReport
public class ScanDataCollectionTypesMessage extends MOPMessage {
    String orgId;
    String emaId;
    String scanId;
    String messagingServiceId;
    private List<ScanType> scanTypes;

    public ScanDataCollectionTypesMessage() {
        super();
    }

    public ScanDataCollectionTypesMessage(String orgId,
                                          String emaId,
                                          String scanId,
                                          String messagingServiceId,
                                          List<ScanType> scanTypes) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.emaId = emaId;
        this.scanId = scanId;
        this.messagingServiceId = messagingServiceId;
        this.scanTypes = scanTypes;
    }
}

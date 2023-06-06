package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class ScanLogMessage extends MOPMessage {
    String orgId;

    String scanId;

    String level;

    String log;

    Long timestamp;

    public ScanLogMessage(String orgId, String scanId, String level, String log, Long timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.scanDataControl)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.level = level;
        this.log = log;
        this.timestamp = timestamp;
    }

    @Override
    public String toLog() {
        return null;
    }
}

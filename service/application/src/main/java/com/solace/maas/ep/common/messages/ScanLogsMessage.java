package com.solace.maas.ep.common.messages;

import ch.qos.logback.classic.Level;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPUHFlag;

public class ScanLogsMessage extends MOPMessage {
    String orgId;

    String scanId;

    Level level;

    String message;

    Long timestamp;

    public ScanLogsMessage(String orgId, String scanId, Level level, String message, Long timestamp) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);

        this.orgId = orgId;
        this.scanId = scanId;
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
    }
}

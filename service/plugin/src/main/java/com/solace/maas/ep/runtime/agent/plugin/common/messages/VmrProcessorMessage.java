package com.solace.maas.ep.runtime.agent.plugin.common.messages;

import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPUHFlag;
import lombok.Data;

@Data
public class VmrProcessorMessage extends MOPMessage {

    private String payload;

    public VmrProcessorMessage(String payload) {
        super();
        withMessageType(MOPMessageType.generic)
                .withProtocol(MOPProtocol.event)
                .withVersion("1")
                .withUhFlag(MOPUHFlag.ignore);
        this.payload = payload;
    }
}

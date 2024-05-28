package com.solace.maas.ep.common.messages;

import com.solace.maas.ep.common.messages.resources.EventBrokerResourceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import lombok.Data;

import java.util.List;

@Data
public abstract class EmaRequestMessage extends MOPMessage {
    private List<EventBrokerResourceConfiguration> resources;
}

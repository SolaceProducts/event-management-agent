package com.solace.maas.ep.event.management.agent.plugin.ibmmq.client.http;

import com.solace.maas.ep.event.management.agent.plugin.ibmmq.processor.event.IbmMqQueueEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class IbmMqQueueResponse {

    private List<IbmMqQueueEvent> queue;
}

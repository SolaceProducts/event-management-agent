package com.solace.maas.ep.event.management.agent.plugin.ibmmq.client.http;

import com.solace.maas.ep.event.management.agent.plugin.ibmmq.processor.event.IbmMqSubscriptionEvent;
import lombok.Data;

import java.util.List;

@Data
public class IbmMqSubscriptionResponse {

    List<IbmMqSubscriptionEvent> subscription;
}

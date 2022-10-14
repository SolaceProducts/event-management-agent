package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.ScanDataMessage;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataPublisher {

    private final SolacePublisher solacePublisher;

    public ScanDataPublisher(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    public void sendScanData(ScanDataMessage message, Map<String, String> topicDetails) {
        String topicString = String.format("sc/ep/runtime/%s/%s/scan/data/v1/%s/%s/%s",
                topicDetails.get("orgId"),
                topicDetails.get("runtimeAgentId"),
                topicDetails.get("messagingServiceId"),
                topicDetails.get("scanId"),
                topicDetails.get("dataCollectionType"));

        solacePublisher.publish(message, topicString);
    }
}

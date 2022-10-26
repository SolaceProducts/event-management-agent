package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.ScanDataMessage;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportPublisher {
    private final SolacePublisher solacePublisher;

    public ScanDataImportPublisher(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    public void uploadScanData(ScanDataMessage message, Map<String, String> topicDetails) {
        String topicString = String.format("sc/ep/runtime/%s/%s/scan/data/v1/%s/%s",
                topicDetails.get("orgId"),
                topicDetails.get("emaId"),
                topicDetails.get("messagingServiceId"),
                topicDetails.get("scanId"));

        solacePublisher.publish(message, topicString);
    }
}

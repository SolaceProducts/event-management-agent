package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataCollectionTypesPublisher {

    private final SolacePublisher solacePublisher;

    public ScanDataCollectionTypesPublisher(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    /**
     * Sends the scan related dataCollectionTypes to EP.
     * <p>
     * The topic:
     * sc/ep/runtime/{orgId}/{emaId}/scan/information/v1/dataCollectionTypes/{messagingServiceId}
     */

    public void sendScanDataCollectionTypes(MOPMessage message, Map<String, String> topicDetails) {
        String topicString = String.format("sc/ep/runtime/%s/%s/scan/information/v1/dataCollectionTypes/%s",
                topicDetails.get("orgId"),
                topicDetails.get("emaId"),
                topicDetails.get("messagingServiceId"));

        solacePublisher.publish(message, topicString);
    }
}

package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.ScanDataStatusMessage;
import com.solace.maas.ep.common.messages.ScanStatusMessage;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanStatusPublisher {

    private final SolacePublisher solacePublisher;

    public ScanStatusPublisher(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    /**
     * Sends the status of the overall scan.
     * topic:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/scan/status/v1/{messagingServiceId}/{scanId}
     */
    public void sendOverallScanStatus(ScanStatusMessage message, Map<String, String> topicDetails) {

        String topicString = String.format("sc/ep/runtime/%s/%s/scan/status/v1/%s/%s",
                topicDetails.get("orgId"),
                topicDetails.get("runtimeAgentId"),
                topicDetails.get("messagingServiceId"),
                topicDetails.get("scanId"));

        solacePublisher.publish(message, topicString);
    }

    /**
     * Sends the status of the overall scan.
     * topic:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/scan/status/v1/{status}/{messagingServiceId}/{scanId}/{dataCollectionType}
     */
    public void sendScanDataStatus(ScanDataStatusMessage message, Map<String, String> topicDetails) {

        String topicString = String.format("sc/ep/runtime/%s/%s/scan/status/v1/%s/%s/%s/%s",
                topicDetails.get("orgId"),
                topicDetails.get("runtimeAgentId"),
                topicDetails.get("status"),
                topicDetails.get("messagingServiceId"),
                topicDetails.get("scanId"),
                topicDetails.get("scanDataType"));

        solacePublisher.publish(message, topicString);
    }
}

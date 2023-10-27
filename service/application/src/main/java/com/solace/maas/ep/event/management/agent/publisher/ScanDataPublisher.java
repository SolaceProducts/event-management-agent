package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanDataPublisher {

    private final SolacePublisher solacePublisher;

    public ScanDataPublisher(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    /**
     * Sends the scan data to EP.
     * <p>
     * The topic for UI-triggered scan:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/scan/status/v1/{messagingServiceId}/{scanId}
     * <p>
     * The topic for imported scan data:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/importScan/v1/{messagingServiceId}
     */

    public void sendScanData(MOPMessage message, Map<String, String> topicDetails) {
        boolean isImport = Boolean.parseBoolean(topicDetails.get("isImportOp"));

        String topicString = isImport ?
                String.format("sc/ep/runtime/%s/%s/importScan/v1/%s",
                        topicDetails.get("orgId"),
                        topicDetails.get("runtimeAgentId"),
                        topicDetails.get("messagingServiceId"))
                :
                String.format("sc/ep/runtime/%s/%s/scan/data/v1/%s/%s/%s",
                        topicDetails.get("orgId"),
                        topicDetails.get("runtimeAgentId"),
                        topicDetails.get("messagingServiceId"),
                        topicDetails.get("scanId"),
                        topicDetails.get("scanType"));

        solacePublisher.publish(message, topicString);
    }
}

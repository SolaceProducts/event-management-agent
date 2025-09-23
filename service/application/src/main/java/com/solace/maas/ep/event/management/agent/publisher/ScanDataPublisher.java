package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_SCAN_EVENT_SENT;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.SCAN_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.STATUS_TAG;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanDataPublisher {

    private final SolacePublisher solacePublisher;
    private final MeterRegistry meterRegistry;

    public ScanDataPublisher(SolacePublisher solacePublisher,
                             MeterRegistry meterRegistry) {
        this.solacePublisher = solacePublisher;
        this.meterRegistry = meterRegistry;
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
                        message.getOrgId(),
                        topicDetails.get("runtimeAgentId"),
                        topicDetails.get("messagingServiceId"))
                :
                String.format("sc/ep/runtime/%s/%s/scan/data/v1/%s/%s/%s",
                        message.getOrgId(),
                        topicDetails.get("runtimeAgentId"),
                        topicDetails.get("messagingServiceId"),
                        topicDetails.get("scanId"),
                        topicDetails.get("scanType"));

        boolean isSuccessful = solacePublisher.publish(message, topicString);

        meterRegistry.counter(MAAS_EMA_SCAN_EVENT_SENT,
                        STATUS_TAG, isSuccessful ? ScanStatus.COMPLETE.name() : ScanStatus.FAILED.name(),
                        SCAN_ID_TAG, topicDetails.get("scanId"),
                        ORG_ID_TAG, message.getOrgId())
                .increment();
    }
}

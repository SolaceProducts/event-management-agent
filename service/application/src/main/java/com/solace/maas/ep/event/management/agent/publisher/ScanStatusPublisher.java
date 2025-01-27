package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.ScanDataStatusMessage;
import com.solace.maas.ep.common.messages.ScanStatusMessage;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptions.ScanOverallStatusException;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptions.ScanStatusException;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_SCAN_EVENT_SENT;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.SCAN_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.STATUS_TAG;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanStatusPublisher {

    private final SolacePublisher solacePublisher;
    private final MeterRegistry meterRegistry;

    public ScanStatusPublisher(SolacePublisher solacePublisher,
                               MeterRegistry meterRegistry) {
        this.solacePublisher = solacePublisher;
        this.meterRegistry = meterRegistry;
    }

    /**
     * Sends the status of the overall scan.
     * topic:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/scan/status/v1/{messagingServiceId}/{scanId}
     */
    public void sendOverallScanStatus(ScanStatusMessage message, Map<String, String> topicDetails) {
        String scanId = topicDetails.get("scanId");
        String scanType = topicDetails.get("scanType");
        String status = topicDetails.get("status");
        String topicString = String.format("sc/ep/runtime/%s/%s/scan/status/v1/%s/%s",
                message.getOrgId(),
                topicDetails.get("runtimeAgentId"),
                topicDetails.get("messagingServiceId"),
                scanId);

        try {
            solacePublisher.publish(message, topicString);
        } catch (Exception e) {
            throw new ScanOverallStatusException("Over all status exception: " + e.getMessage(),
                    Map.of(scanId, List.of(e)), "Overall status", Arrays.asList(scanType.split(",")), ScanStatus.valueOf(status));
        } finally {
            meterRegistry.counter(MAAS_EMA_SCAN_EVENT_SENT, STATUS_TAG, status, SCAN_ID_TAG, scanId,
                    ORG_ID_TAG, topicDetails.get("orgId")).increment();
        }
    }

    /**
     * Sends the status of the overall scan.
     * topic:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/scan/status/v1/{status}/{messagingServiceId}/{scanId}/{dataCollectionType}
     */
    public void sendScanDataStatus(ScanDataStatusMessage message, Map<String, String> topicDetails) {
        String scanId = topicDetails.get("scanId");
        String scanType = topicDetails.get("scanDataType");
        String status = topicDetails.get("status");

        String topicString = String.format("sc/ep/runtime/%s/%s/scan/status/v1/%s/%s/%s/%s",
                message.getOrgId(),
                topicDetails.get("runtimeAgentId"),
                status,
                topicDetails.get("messagingServiceId"),
                scanId,
                scanType);

        try {
            solacePublisher.publish(message, topicString);
        } catch (Exception e) {
            throw new ScanStatusException("Route status exception: " + e.getMessage(),
                    Map.of(scanId, List.of(e)), "Route status", List.of(scanType), ScanStatus.valueOf(status));
        }  finally {
            meterRegistry.counter(MAAS_EMA_SCAN_EVENT_SENT, STATUS_TAG, status, SCAN_ID_TAG, scanId,
                    ORG_ID_TAG, topicDetails.get("orgId")).increment();
        }
    }
}

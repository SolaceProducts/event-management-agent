package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanDataStatusMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.publisher.ScanStatusPublisher;
import com.solace.maas.ep.event.management.agent.route.ep.exceptions.ScanStatusException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
@SuppressWarnings("unchecked")
public class ScanStatusPerRouteProcessor implements Processor {
    private final String orgId;
    private final String runtimeAgentId;

    private final ScanStatusPublisher scanStatusPublisher;

    @Autowired
    public ScanStatusPerRouteProcessor(ScanStatusPublisher scanStatusPublisher, EventPortalProperties eventPortalProperties) {
        super();

        this.scanStatusPublisher = scanStatusPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        HashMap<String, String> topicDetails = new HashMap<>();
        Map<String, Object> properties = exchange.getIn().getHeaders();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);
        ScanStatus status = (ScanStatus) properties.get(RouteConstants.SCAN_STATUS);
        String description = (String) properties.get(RouteConstants.SCAN_STATUS_DESC);

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);
        topicDetails.put("status", status.name());
        topicDetails.put("scanDataType", scanType);

        ScanDataStatusMessage scanDataStatusMessage = new
                ScanDataStatusMessage(orgId, scanId, status.name(), description, scanType);

        try {
            scanStatusPublisher.sendScanDataStatus(scanDataStatusMessage, topicDetails);
        } catch (Exception e) {
            throw new ScanStatusException("Scan status per route processor exception: " + e.getMessage(),
                    Map.of(scanId, List.of(e)), "Per route status", List.of(scanType), status);
        }
    }
}

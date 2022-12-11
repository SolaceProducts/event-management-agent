package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanStatusMessage;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanStatusOverAllProcessor implements Processor {

    private final String orgId;
    private final String runtimeAgentId;

    private final ScanStatusPublisher scanStatusPublisher;

    @Autowired
    public ScanStatusOverAllProcessor(ScanStatusPublisher scanStatusPublisher, EventPortalProperties eventPortalProperties) {
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
        ScanStatus status = (ScanStatus) properties.get(RouteConstants.SCAN_STATUS);
        String description = (String) properties.get(RouteConstants.SCAN_STATUS_DESC);

        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);
        List<String> scanTypes = Arrays.asList(scanType.split(","));

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);

        ScanStatusMessage generalStatusMessage = new
                ScanStatusMessage(orgId, scanId, status.name(), description, scanTypes);

        try {
            scanStatusPublisher.sendOverallScanStatus(generalStatusMessage, topicDetails);
        } catch (Exception e) {
            throw new ScanStatusException("Over all status exception: " + e.getMessage(),
                    Map.of(scanId, List.of(e)), "Overall status", scanTypes, status);
        }
    }
}

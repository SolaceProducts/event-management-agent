package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanDataMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataImportPublisher;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportProcessor implements Processor {
    private final ScanDataImportPublisher scanDataImportPublisher;
    private final String orgId;
    private final String emaId;

    @Autowired
    public ScanDataImportProcessor(ScanDataImportPublisher scanDataImportPublisher,
                                   EventPortalProperties eventPortalProperties) {
        super();

        this.scanDataImportPublisher = scanDataImportPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        emaId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> topicDetails = new HashMap<>();

        Map<String, Object> properties = exchange.getIn().getHeaders();
        String body = (String) exchange.getIn().getBody();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);

        ScanDataMessage scanDataMessage =
                new ScanDataMessage(orgId, scanId, scanType, body, Instant.now().toString());

        topicDetails.put("orgId", orgId);
        topicDetails.put("emaId", emaId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);

        scanDataImportPublisher.uploadScanData(scanDataMessage, topicDetails);
    }
}

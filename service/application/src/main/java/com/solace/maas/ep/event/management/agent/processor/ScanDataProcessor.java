package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanDataMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataPublisher;
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
public class ScanDataProcessor implements Processor {

    private final ScanDataPublisher scanDataPublisher;
    private final String orgId;
    private final String runtimeAgentId;

    @Autowired
    public ScanDataProcessor(ScanDataPublisher scanDataPublisher, EventPortalProperties eventPortalProperties) {
        super();

        this.scanDataPublisher = scanDataPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> topicDetails = new HashMap<>();

        Map<String, Object> properties = exchange.getIn().getHeaders();
        String body = (String) exchange.getIn().getBody();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);
        Boolean isImportOp = (Boolean) properties.get(RouteConstants.IS_IMPORTED_DATA);

        ScanDataMessage scanDataMessage =
                new ScanDataMessage(orgId, scanId, scanType, body, Instant.now().toString());

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);
        topicDetails.put("scanType", scanType);
        topicDetails.put("isImportOp", String.valueOf(isImportOp));

        scanDataPublisher.sendScanData(scanDataMessage, topicDetails);
    }
}

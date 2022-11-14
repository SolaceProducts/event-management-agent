package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ImportDataMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataPublishImportScanEventProcessor implements Processor {
    private final String orgId;
    private final String runtimeAgentId;

    private final ScanDataPublisher scanDataPublisher;

    @Autowired
    public ScanDataPublishImportScanEventProcessor(ScanDataPublisher scanDataPublisher, EventPortalProperties eventPortalProperties) {
        super();

        this.scanDataPublisher = scanDataPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(RouteConstants.IS_DATA_IMPORT, true);

        Map<String, String> topicDetails = new HashMap<>();

        Map<String, Object> properties = exchange.getIn().getHeaders();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        Boolean isImportOp = (Boolean) properties.get(RouteConstants.IS_DATA_IMPORT);

        ImportDataMessage importDataMessage =
                new ImportDataMessage(orgId, scanId, messagingServiceId);

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);
        topicDetails.put("isImportOp", String.valueOf(isImportOp));

        scanDataPublisher.sendScanData(importDataMessage, topicDetails);
    }
}
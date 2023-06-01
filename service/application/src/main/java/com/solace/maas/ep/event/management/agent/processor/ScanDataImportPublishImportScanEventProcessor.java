package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanDataImportMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataPublisher;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportPublishImportScanEventProcessor implements Processor {
    private final String orgId;
    private final String runtimeAgentId;
    private final ScanDataPublisher scanDataPublisher;

    public ScanDataImportPublishImportScanEventProcessor(ScanDataPublisher scanDataPublisher,
                                                         EventPortalProperties eventPortalProperties) {

        this.scanDataPublisher = scanDataPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> topicDetails = new HashMap<>();
        exchange.getIn().setHeader(RouteConstants.IS_DATA_IMPORT, true);

        List<MetaInfFileDetailsBO> files = (List<MetaInfFileDetailsBO>) exchange.getIn().getBody();

        List<String> scanTypes = files.stream()
                .map(MetaInfFileDetailsBO::getDataEntityType)
                .collect(Collectors.toUnmodifiableList());

        String traceId = (String) exchange.getProperty(RouteConstants.TRACE_ID);

        Map<String, Object> properties = exchange.getIn().getHeaders();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        Boolean isImportOp = (Boolean) properties.get(RouteConstants.IS_DATA_IMPORT);

        ScanDataImportMessage importDataMessage =
                new ScanDataImportMessage(orgId, scanId, traceId, messagingServiceId, runtimeAgentId, scanTypes);

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);
        topicDetails.put("isImportOp", String.valueOf(isImportOp));

        log.debug("Performing handshake with EP. Sending ScanDataImportMessage: {}", importDataMessage);

        scanDataPublisher.sendScanData(importDataMessage, topicDetails);
    }
}
package com.solace.maas.ep.event.management.agent.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.solace.maas.ep.common.messages.ScanLogMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.publisher.ScanLogsPublisher;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
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
public class ScanLogsProcessor implements Processor {
    private final String orgId;
    private final String runtimeAgentId;

    private final ScanLogsPublisher logDataPublisher;

    @Autowired
    public ScanLogsProcessor(ScanLogsPublisher logDataPublisher, EventPortalProperties eventPortalProperties) {
        super();

        this.logDataPublisher = logDataPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> topicDetails = new HashMap<>();

        Map<String, Object> properties = exchange.getIn().getHeaders();
        ILoggingEvent event = (ILoggingEvent) exchange.getIn().getBody();
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String traceId = (String) properties.get(RouteConstants.TRACE_ID);
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        ScanLogMessage logDataMessage = new ScanLogMessage(orgId, scanId, traceId, event.getLevel().toString(),
                String.format("%s%s", event.getFormattedMessage(), "\n"), event.getTimeStamp());

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);

        logDataPublisher.sendScanLogData(logDataMessage, topicDetails);
    }
}

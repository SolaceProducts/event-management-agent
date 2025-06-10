package com.solace.maas.ep.event.management.agent.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.solace.maas.ep.common.messages.ScanLogMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.common.ProcessorUtils;
import com.solace.maas.ep.event.management.agent.publisher.ScanLogsPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanLogsProcessor implements Processor {
    private final EventPortalProperties eventPortalProperties;
    private final ScanLogsPublisher logDataPublisher;

    @Autowired
    public ScanLogsProcessor(ScanLogsPublisher logDataPublisher, EventPortalProperties eventPortalProperties) {
        super();
        this.logDataPublisher = logDataPublisher;
        this.eventPortalProperties = eventPortalProperties;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> topicDetails = new HashMap<>();

        Map<String, Object> properties = exchange.getIn().getHeaders();
        ILoggingEvent event = (ILoggingEvent) exchange.getIn().getBody();
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String traceId = (String) properties.get(RouteConstants.TRACE_ID);
        String orgId = ProcessorUtils.determineOrganizationId(eventPortalProperties, exchange);
        String actorId = (String) properties.get(RouteConstants.ACTOR_ID);
        String originOrgId = (String) properties.get(RouteConstants.ORIGIN_ORG_ID);
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        ScanLogMessage logDataMessage = new ScanLogMessage(orgId, originOrgId, scanId, traceId, actorId, event.getLevel().toString(),
                String.format("%s%s", event.getFormattedMessage(), "\n"), event.getTimeStamp());

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", eventPortalProperties.getRuntimeAgentId());
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);

        logDataPublisher.sendScanLogData(logDataMessage, topicDetails);
    }
}

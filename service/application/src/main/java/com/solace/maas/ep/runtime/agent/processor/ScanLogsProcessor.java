package com.solace.maas.ep.runtime.agent.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.solace.maas.ep.common.messages.ScanLogsMessage;
import com.solace.maas.ep.runtime.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.publisher.ScanLogsPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
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
        String timestamp = Instant.now().toString();

        Map<String, Object> properties = exchange.getIn().getHeaders();
        ILoggingEvent event = (ILoggingEvent) exchange.getIn().getBody();
        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        JSONObject json = new JSONObject();
        json.put("timestamp", event.getTimeStamp());
        json.put("level", event.getLevel());
        json.put("message", String.format("%s%s", event.getFormattedMessage(), "\n"));

        ScanLogsMessage logDataMessage =
                new ScanLogsMessage(orgId, scanId, json.toString(), timestamp);

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put("scanId", scanId);

        logDataPublisher.sendScanLogData(logDataMessage, topicDetails);

        log.info("Scan logs have been published successfully.");
    }
}

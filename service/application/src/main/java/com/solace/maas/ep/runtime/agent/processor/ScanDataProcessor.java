package com.solace.maas.ep.runtime.agent.processor;

import com.solace.maas.ep.common.messages.ScanDataMessage;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.publisher.ScanDataPublisher;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;


@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataProcessor implements Processor {

    private final ScanDataPublisher scanDataPublisher;

    @Autowired
    public ScanDataProcessor(ScanDataPublisher scanDataPublisher) {
        super();
        this.scanDataPublisher = scanDataPublisher;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> properties = exchange.getIn().getHeaders();
        String body = (String) exchange.getIn().getBody();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String dataCollectionType = (String) properties.get(RouteConstants.SCAN_TYPE);

        ScanDataMessage scanDataMessage =
                new ScanDataMessage(messagingServiceId, scanId, dataCollectionType, body, Instant.now().toString());

        scanDataPublisher.sendScanData(scanDataMessage);
    }
}

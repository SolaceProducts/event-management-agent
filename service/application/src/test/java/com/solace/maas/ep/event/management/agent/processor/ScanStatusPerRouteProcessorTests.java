package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanDataStatusMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.publisher.ScanStatusPublisher;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class ScanStatusPerRouteProcessorTests {

    @Mock
    ScanStatusPublisher scanStatusPublisher;

    @Mock
    EventPortalProperties eventPortalProperties;

    @Autowired
    private CamelContext camelContext;

    @InjectMocks
    private ScanStatusPerRouteProcessor scanStatusPerRouteProcessor;

    @SneakyThrows
    @Test
    public void testScanStatusProcessor() {

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");

        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);

        ScanDataStatusMessage scanDataStatusMessage = new
                ScanDataStatusMessage(null, "scan1", ScanStatus.IN_PROGRESS.name(), "", "queueListing");

        HashMap<String, String> topicDetails = new HashMap<>();
        topicDetails.put("orgId", null);
        topicDetails.put("runtimeAgentId", null);
        topicDetails.put("messagingServiceId", "messagingServiceId");
        topicDetails.put("scanId", "scan1");
        topicDetails.put("status", ScanStatus.IN_PROGRESS.name());
        topicDetails.put("scanDataType", "queueListing");

        scanStatusPerRouteProcessor.process(exchange);

        assertThatNoException();
        verify(scanStatusPublisher, times(1)).sendScanDataStatus(scanDataStatusMessage, topicDetails);
    }
}

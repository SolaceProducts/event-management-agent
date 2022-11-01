package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatusType;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class ScanStatusProcessorTests {

    @Mock
    ScanStatusPublisher scanStatusPublisher;

    @Mock
    EventPortalProperties eventPortalProperties;

    @Autowired
    private CamelContext camelContext;

    @InjectMocks
    private ScanStatusProcessor scanStatusProcessor;

    @SneakyThrows
    @Test
    public void testScanStatusProcessor() {

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.OVERALL);

        exchange.getIn().setBody(List.of("queueListing"));

        scanStatusProcessor.process(exchange);

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testScanDataStatusProcessor() {

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.PER_ROUTE);

        scanStatusProcessor.process(exchange);

        assertThatNoException();
    }
}

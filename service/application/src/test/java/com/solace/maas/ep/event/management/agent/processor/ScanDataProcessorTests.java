package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanDataMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataPublisher;
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

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class ScanDataProcessorTests {

    @Mock
    ScanDataPublisher scanDataPublisher;

    @Mock
    EventPortalProperties eventPortalProperties;

    @Autowired
    private CamelContext camelContext;

    @InjectMocks
    private ScanDataProcessor scanDataProcessor;

    @SneakyThrows
    @Test
    public void testProcessor() {

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.TRACE_ID, "traceId");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.TOPIC, "test/ep/v1");
        exchange.getIn().setHeader(RouteConstants.IS_DATA_IMPORT, false);

        exchange.getIn().setBody("test data");

        scanDataProcessor.process(exchange);

        assertThatNoException();
    }

    @Test
    public void testScanMessageMOPProtocol() {
        ScanDataMessage scanDataMessage = new ScanDataMessage(
                "orgId",
                "scanId",
                "traceId",
                "actorId",
                "scanType",
                " body",
                Instant.now().toString());

        assertThat(scanDataMessage.getMopProtocol()).isEqualTo(MOPProtocol.scanData);
    }
}

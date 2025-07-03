package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ScanStatusMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import lombok.SneakyThrows;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class ScanStatusOverAllProcessorTests {

    @Mock
    EventPortalProperties eventPortalProperties;

    @Autowired
    private CamelContext camelContext;

    @InjectMocks
    private ScanStatusOverAllProcessor scanStatusOverAllProcessor;

    @SneakyThrows
    @Test
    public void testScanStatusOverAllProcessor() {
        List<String> scanTypes = List.of("topicListing", "topicConfiguration", "overrideTopicConfiguration");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.TRACE_ID, "traceId");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, StringUtils.join(scanTypes, ","));
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_DESC, "description");

        HashMap<String, String> topicDetails = new HashMap<>();
        topicDetails.put("orgId", null);
        topicDetails.put("runtimeAgentId", null);
        topicDetails.put("messagingServiceId", "messagingServiceId");
        topicDetails.put("scanId", "scan1");

        scanStatusOverAllProcessor.process(exchange);

        assertThatNoException();
    }

    @Test
    public void testScanStatusMessageProtocol() {
        ScanStatusMessage scanStatusMessage = new ScanStatusMessage(
                null,
                null,
                "scan1",
                "traceId",
                "actorId",
                ScanStatus.IN_PROGRESS.name(),
                "description",
                List.of("scanTypes"));

        assertThat(scanStatusMessage.getMopProtocol()).isEqualTo(MOPProtocol.scanDataControl);
    }
}
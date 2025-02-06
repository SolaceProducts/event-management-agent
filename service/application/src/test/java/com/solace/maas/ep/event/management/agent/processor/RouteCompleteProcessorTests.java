package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.service.ScanStatusService;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SuppressWarnings("CPD-START")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class RouteCompleteProcessorTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    CamelContext camelContext;

    @Mock
    ScanStatusService scanStatusService;

    @Mock
    ProducerTemplate producerTemplate;

    @Mock
    EventPortalProperties eventPortalProperties;

    @InjectMocks
    RouteCompleteProcessorImpl routeCompleteProcessor;

    @SneakyThrows
    @Test
    public void testRouteCompleteProcessor() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "scanType");
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, "groupId");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");

        exchange.getIn().setBody("test exchange");

        when(scanStatusService.save(any(String.class), any(String.class), any(ScanStatus.class)))
                .thenReturn(ScanStatusEntity.builder().build());

        routeCompleteProcessor.process(exchange);

        assertThatNoException();

        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());
        routeCompleteProcessor.process(exchange);

        exception.expect(Exception.class);
    }
}
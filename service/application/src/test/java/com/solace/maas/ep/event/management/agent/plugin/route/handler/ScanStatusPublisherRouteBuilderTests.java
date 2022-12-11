package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.processor.ScanStatusOverAllProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanStatusPerRouteProcessor;
import com.solace.maas.ep.event.management.agent.publisher.ScanStatusPublisher;
import com.solace.maas.ep.event.management.agent.route.ep.ScanStatusPublisherRouteBuilder;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.mock;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class ScanStatusPublisherRouteBuilderTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:mockPerRouteScanStatusPublisherResult")
    private MockEndpoint mockPerRouteScanStatusPublisherResult;

    @EndpointInject("mock:direct:mockPerRouteInProgressScanStatusPublisherResult")
    private MockEndpoint mockPerRouteInProgressScanStatusPublisherResult;

    @EndpointInject("mock:direct:mockOverallScanStatusPublisher")
    private MockEndpoint mockOverallScanStatusPublisher;


    @Test
    @SneakyThrows
    public void testMockRouteWithCompleteStatus() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");

        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);

        AdviceWith.adviceWith(camelContext, "perRouteScanStatusPublisher",
                route -> {
                    route.replaceFromWith("direct:perRouteScanStatusPublisher");
                    route.weaveAddLast().to("mock:direct:mockPerRouteScanStatusPublisherResult");
                });

        mockPerRouteScanStatusPublisherResult.expectedMessageCount(1);
        producerTemplate.send("direct:perRouteScanStatusPublisher", exchange);
        mockPerRouteScanStatusPublisherResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMockRouteWithInProgressStatus() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");

        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);

        exchange.getIn().setBody(List.of("queueListing"));

        AdviceWith.adviceWith(camelContext, "perRouteScanStatusPublisher",
                route -> {
                    route.replaceFromWith("direct:perRouteScanStatusPublisher");
                    route.weaveAddLast().to("mock:direct:mockPerRouteInProgressScanStatusPublisherResult");
                });

        mockPerRouteInProgressScanStatusPublisherResult.expectedMessageCount(1);
        producerTemplate.send("direct:perRouteScanStatusPublisher", exchange);
        mockPerRouteInProgressScanStatusPublisherResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMockRouteOverallStatus() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");

        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);

        exchange.getIn().setBody(List.of("queueListing"));

        AdviceWith.adviceWith(camelContext, "overallScanStatusPublisher",
                route -> {
                    route.replaceFromWith("direct:overallScanStatusPublisher");
                    route.weaveAddLast().to("mock:direct:mockOverallScanStatusPublisher");
                });

        mockOverallScanStatusPublisher.expectedMessageCount(1);
        producerTemplate.send("direct:overallScanStatusPublisher", exchange);
        mockOverallScanStatusPublisher.assertIsSatisfied();
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            SolacePublisher solacePublisher = mock(SolacePublisher.class);
            EventPortalProperties eventPortalProperties = mock(EventPortalProperties.class);

            ScanStatusPublisher scanStatusPublisher =
                    new ScanStatusPublisher(solacePublisher);
            ScanStatusPerRouteProcessor scanStatusPerRouteProcessor =
                    new ScanStatusPerRouteProcessor(scanStatusPublisher, eventPortalProperties);
            ScanStatusOverAllProcessor scanStatusOverAllStatusProcessor =
                    new ScanStatusOverAllProcessor(scanStatusPublisher, eventPortalProperties);
            return new ScanStatusPublisherRouteBuilder(scanStatusOverAllStatusProcessor, scanStatusPerRouteProcessor);
        }
    }
}

package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.ScanStatusMarkerAndLoggerRouteBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Slf4j
public class ScanStatusMarkerAndLoggerRouteBuilderTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:mockMarkRouteScanStatusInProgress")
    private MockEndpoint mockMarkRouteScanStatusInProgressResult;

    @EndpointInject("mock:direct:mockMarkRouteScanStatusComplete")
    private MockEndpoint mockMarkRouteScanStatusCompleteResult;

    @EndpointInject("mock:direct:mockMarkRouteImportStatusInProgress")
    private MockEndpoint mockMarkRouteImportStatusInProgressResult;

    @EndpointInject("mock:direct:mockMarkRouteImportStatusComplete")
    private MockEndpoint mockMarkRouteImportStatusCompleteResult;


    @Test
    @SneakyThrows
    public void testMarkRouteScanStatusInProgress() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
        exchange.getIn().setHeader(RouteConstants.TRACE_ID, "1234");

        AdviceWith.adviceWith(camelContext, "markRouteScanStatusInProgress",
                route -> {
                    route.replaceFromWith("direct:markRouteScanStatusInProgress");
                    route.weaveAddLast().to("mock:direct:mockMarkRouteScanStatusInProgress");
                });

        mockMarkRouteScanStatusInProgressResult.expectedMessageCount(1);
        producerTemplate.send("direct:markRouteScanStatusInProgress", exchange);
        mockMarkRouteScanStatusInProgressResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMarkRouteScanStatusComplete() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);
        exchange.getIn().setHeader(RouteConstants.TRACE_ID, "1234");

        AdviceWith.adviceWith(camelContext, "markRouteScanStatusComplete",
                route -> {
                    route.replaceFromWith("direct:markRouteScanStatusComplete");
                    route.weaveByToUri("direct:processScanStatusAsComplete?block=false&failIfNoConsumers=false")
                            .replace().to("mock:processScanStatusAsComplete");
                    route.weaveAddLast().to("mock:direct:mockMarkRouteScanStatusComplete");
                });

        mockMarkRouteScanStatusCompleteResult.expectedMessageCount(1);
        producerTemplate.send("direct:markRouteScanStatusComplete", exchange);
        mockMarkRouteScanStatusCompleteResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMarkRouteImportStatusInProgress() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
        exchange.getIn().setHeader(RouteConstants.TRACE_ID, "1234");

        AdviceWith.adviceWith(camelContext, "markRouteImportStatusInProgress",
                route -> {
                    route.replaceFromWith("direct:markRouteImportStatusInProgress");
                    route.weaveAddLast().to("mock:direct:mockMarkRouteImportStatusInProgress");
                });

        mockMarkRouteImportStatusInProgressResult.expectedMessageCount(1);
        producerTemplate.send("direct:markRouteImportStatusInProgress", exchange);
        mockMarkRouteImportStatusInProgressResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMarkRouteImportStatusComplete() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);
        exchange.getIn().setHeader(RouteConstants.TRACE_ID, "1234");

        AdviceWith.adviceWith(camelContext, "markRouteImportStatusComplete",
                route -> {
                    route.replaceFromWith("direct:markRouteImportStatusComplete");
                    route.weaveByToUri("direct:processScanStatusAsComplete?block=false&failIfNoConsumers=false")
                            .replace().to("mock:processScanStatusAsComplete");
                    route.weaveAddLast().to("mock:direct:mockMarkRouteImportStatusComplete");
                });

        mockMarkRouteImportStatusCompleteResult.expectedMessageCount(1);
        producerTemplate.send("direct:markRouteImportStatusComplete", exchange);
        mockMarkRouteImportStatusCompleteResult.assertIsSatisfied();
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            return new ScanStatusMarkerAndLoggerRouteBuilder();
        }
    }
}

package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.common.model.ScanStatus;
import com.solace.maas.ep.common.model.ScanStatusType;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.processor.ScanStatusProcessor;
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

    @EndpointInject("mock:direct:result")
    private MockEndpoint mockResult;


    @Test
    @SneakyThrows
    public void testMockRouteWithCompleteStatus() throws Exception {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");

        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.PER_ROUTE);

        AdviceWith.adviceWith(camelContext, "scanStatusPublisher",
                route -> {
                    route.replaceFromWith("direct:scanStatusPublisher");
                    route.weaveAddLast().to("mock:direct:result");
                });

        mockResult.expectedMessageCount(1);
        producerTemplate.send("direct:scanStatusPublisher", exchange);
        mockResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMockRouteWithInProgressStatus() throws Exception {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPES, List.of("queueListing"));

        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.OVERALL);

        AdviceWith.adviceWith(camelContext, "scanStatusPublisher",
                route -> {
                    route.replaceFromWith("direct:scanStatusPublisher");
                    route.weaveAddLast().to("mock:direct:result");
                });

        mockResult.expectedMessageCount(1);
        producerTemplate.send("direct:scanStatusPublisher", exchange);
        mockResult.assertIsSatisfied();
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            SolacePublisher solacePublisher = mock(SolacePublisher.class);
            EventPortalProperties eventPortalProperties = mock(EventPortalProperties.class);

            ScanStatusPublisher scanStatusPublisher = new ScanStatusPublisher(solacePublisher);
            ScanStatusProcessor scanStatusProcessor = new ScanStatusProcessor(scanStatusPublisher, eventPortalProperties);
            return new ScanStatusPublisherRouteBuilder(scanStatusProcessor);
        }
    }
}

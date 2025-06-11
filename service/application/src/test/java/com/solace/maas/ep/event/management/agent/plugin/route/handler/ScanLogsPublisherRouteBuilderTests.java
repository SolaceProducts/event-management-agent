package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.solace.maas.ep.common.messages.ScanLogMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.processor.ScanLogsProcessor;
import com.solace.maas.ep.event.management.agent.publisher.ScanLogsPublisher;
import com.solace.maas.ep.event.management.agent.route.ep.ScanLogsPublisherRouteBuilder;
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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class ScanLogsPublisherRouteBuilderTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:result")
    private MockEndpoint mockResult;


    @Test
    @SneakyThrows
    public void testMockRoute() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ILoggingEvent event = new LoggingEvent(null, logger, Level.DEBUG,
                "test message", new Throwable("throwable message"), null);

        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.TRACE_ID, "traceId");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");
        exchange.getIn().setHeader(RouteConstants.TOPIC, "test/ep/v1");

        exchange.getIn().setBody(event);

        AdviceWith.adviceWith(camelContext, "scanLogsPublisher",
                route -> {
                    route.replaceFromWith("direct:scanLogsPublisher");
                    route.weaveAddLast().to("mock:direct:result");
                });

        mockResult.expectedMessageCount(1);
        producerTemplate.send("direct:scanLogsPublisher", exchange);
        mockResult.assertIsSatisfied();
    }

    @Test
    public void testScanLogMessageMOPProtocol() {
        ScanLogMessage scanLogMessage = new ScanLogMessage(
                "orgId",
                "originOrgId",
                "scanId",
                "traceId",
                "actorId",
                "level",
                "log", Instant.now().toEpochMilli());

        assertThat(scanLogMessage.getMopProtocol()).isEqualTo(MOPProtocol.scanDataControl);
    }


    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            SolacePublisher solacePublisher = mock(SolacePublisher.class);
            EventPortalProperties eventPortalProperties = mock(EventPortalProperties.class);

            ScanLogsPublisher scanLogsPublisher = new ScanLogsPublisher(solacePublisher);
            ScanLogsProcessor scanLogsProcessor = new ScanLogsProcessor(scanLogsPublisher, eventPortalProperties);
            return new ScanLogsPublisherRouteBuilder(scanLogsProcessor);
        }
    }
}

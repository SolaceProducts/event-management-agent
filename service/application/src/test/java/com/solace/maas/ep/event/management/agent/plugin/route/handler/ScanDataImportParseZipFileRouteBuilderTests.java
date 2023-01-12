package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.route.manualImport.ScanDataImportParseZipFileRouteBuilder;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.AggregateDefinition;
import org.apache.camel.model.PollEnrichDefinition;
import org.apache.camel.model.UnmarshalDefinition;
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

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class ScanDataImportParseZipFileRouteBuilderTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:mockCheckZipSizeAndUnzipFilesResult")
    private MockEndpoint mockCheckZipSizeAndUnzipFilesResult;

    @Test
    @SneakyThrows
    public void testMockScanDataImportParseZipFileRoute() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");

        AdviceWith.adviceWith(camelContext, "checkZipSizeAndUnzipFiles",
                route -> {
                    route.weaveByType(PollEnrichDefinition.class).replace().to("mock:poll");
                    route.weaveByType(UnmarshalDefinition.class).replace().to("mock:unmarshal");
                    route.weaveByType(AggregateDefinition.class).replace().to("mock:aggregation");
                    route.weaveAddLast().to("mock:direct:mockCheckZipSizeAndUnzipFilesResult");
                });

        mockCheckZipSizeAndUnzipFilesResult.expectedMessageCount(1);
        producerTemplate.send("direct:checkZipSizeAndUnzipFiles", exchange);
        mockCheckZipSizeAndUnzipFilesResult.assertIsSatisfied();
    }


    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            return new ScanDataImportParseZipFileRouteBuilder();
        }
    }
}

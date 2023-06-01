package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPersistScanFilesProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportStatusProcessor;
import com.solace.maas.ep.event.management.agent.route.manualImport.ScanDataImportStreamFilesRouteBuilder;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.PollEnrichDefinition;
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
import java.util.UUID;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.IMPORT_ID;
import static org.mockito.Mockito.mock;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class ScanDataImportStreamFilesRouteBuilderTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:parseAndStreamImportResult")
    private MockEndpoint mockParseAndStreamImportResult;

    @EndpointInject("mock:direct:streamImportFilesResult")
    private MockEndpoint mockStreamImportFilesResult;

    @EndpointInject("mock:direct:processEndOfFileImportStatusResult")
    private MockEndpoint mockProcessEndOfFileImportStatusResult;

    @Test
    @SneakyThrows
    public void testMockParseAndStreamImportFilesRoute() {
        List<MetaInfFileDetailsBO> files = List.of(MetaInfFileDetailsBO.builder()
                .fileName("topicListing.json")
                .dataEntityType("topicListing")
                .build());

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(IMPORT_ID, UUID.randomUUID().toString());
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setBody(files);

        AdviceWith.adviceWith(camelContext, "parseAndStreamImportFiles",
                route -> {
                    route.weaveByToUri("direct:markRouteImportStatusInProgress?block=false&failIfNoConsumers=false")
                            .replace().to("mock:markRouteImportStatusInProgress");
                    route.weaveByType(PollEnrichDefinition.class).replace().process(exchange1 ->
                            exchange1.getIn().setHeader("CamelFileName", "topicListing.json"));
                    route.weaveByToUri("direct:streamImportFiles").replace().to("mock:streamImportFiles");
                    route.weaveAddLast().to("mock:direct:parseAndStreamImportResult");
                });

        mockParseAndStreamImportResult.expectedMessageCount(1);
        producerTemplate.send("direct:parseAndStreamImportFiles", exchange);
        mockParseAndStreamImportResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMockStreamImportFilesRoute() {

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(IMPORT_ID, UUID.randomUUID().toString());
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(Exchange.SPLIT_COMPLETE, true);
        exchange.getIn().setBody("test data");

        AdviceWith.adviceWith(camelContext, "streamImportFiles",
                route -> {
                    route.weaveByToUri("direct:importToEP").replace().to("mock:importToEP");
                    route.weaveByToUri("direct:processEndOfFileImportStatus").replace().to("mock:processEndOfFileImportStatus");
                    route.weaveAddLast().to("mock:direct:streamImportFilesResult");
                });

        mockStreamImportFilesResult.expectedMessageCount(1);
        producerTemplate.send("direct:streamImportFiles", exchange);
        mockStreamImportFilesResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMockProcessEndOfFileImportStatusRoute() {

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(IMPORT_ID, UUID.randomUUID().toString());
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setBody("test data");

        AdviceWith.adviceWith(camelContext, "processEndOfFileImportStatus",
                route -> {
                    route.weaveByToUri("direct:markRouteImportStatusComplete?block=false&failIfNoConsumers=false").replace().to("mock:markRouteImportStatusComplete");
                    route.weaveAddLast().to("mock:direct:processEndOfFileImportStatusResult");
                });

        mockProcessEndOfFileImportStatusResult.expectedMessageCount(1);
        producerTemplate.send("direct:processEndOfFileImportStatus", exchange);
        mockProcessEndOfFileImportStatusResult.assertIsSatisfied();
    }


    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            ScanDataImportStatusProcessor scanDataImportStatusProcessor =
                    mock(ScanDataImportStatusProcessor.class);
            ScanDataImportPersistScanFilesProcessor scanDataImportFilePersistingProcessor =
                    mock(ScanDataImportPersistScanFilesProcessor.class);

            return new ScanDataImportStreamFilesRouteBuilder(scanDataImportStatusProcessor,
                    scanDataImportFilePersistingProcessor);
        }
    }
}

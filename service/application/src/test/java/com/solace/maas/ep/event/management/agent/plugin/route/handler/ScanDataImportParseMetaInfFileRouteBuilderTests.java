package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportOverAllStatusProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportParseMetaInfFileProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPersistFilePathsProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPersistScanDataProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPublishImportScanEventProcessor;
import com.solace.maas.ep.event.management.agent.route.manualImport.ScanDataImportParseMetaInfFileRouteBuilder;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static com.solace.maas.ep.event.management.agent.plugin.route.handler.ScanDataImportParseMetaInfFileRouteBuilderTests.TestConfig.getMetaInfJson;
import static org.mockito.Mockito.mock;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class ScanDataImportParseMetaInfFileRouteBuilderTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:mockParseMetaInfoResult")
    private MockEndpoint mockParseMetaInfoResult;

    @EndpointInject("mock:direct:mockSendOverAllInProgressImportStatusResult")
    private MockEndpoint mockSendOverAllInProgressImportStatusResult;

    @Test
    @SneakyThrows
    public void testMockParseMetaInfoAndPerformHandShakeWithEP() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");

        AdviceWith.adviceWith(camelContext, "parseMetaInfoAndPerformHandShakeWithEP",
                route -> {
                    route.weaveByType(PollEnrichDefinition.class).replace().process(exchange1 -> {
                        exchange1.getIn().setHeader("CamelFileName", "META_INF.json");
                        exchange1.getIn().setBody(getMetaInfJson());
                    });
                    route.weaveAddLast().to("mock:direct:mockParseMetaInfoResult");
                });

        mockParseMetaInfoResult.expectedMessageCount(1);
        producerTemplate.send("direct:parseMetaInfoAndPerformHandShakeWithEP", exchange);
        mockParseMetaInfoResult.assertIsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testMockSendOverAllInProgressImportStatusRoute() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");

        AdviceWith.adviceWith(camelContext, "sendOverAllInProgressImportStatus",
                route -> {
                    route.weaveByToUri("direct:overallScanStatusPublisher?block=false&failIfNoConsumers=false")
                            .replace().to("mock:overallScanStatusPublisher");
                    route.weaveAddLast().to("mock:direct:mockSendOverAllInProgressImportStatusResult");
                });

        mockSendOverAllInProgressImportStatusResult.expectedMessageCount(1);
        producerTemplate.send("direct:sendOverAllInProgressImportStatus", exchange);
        mockSendOverAllInProgressImportStatusResult.assertIsSatisfied();
    }


    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            ScanDataImportParseMetaInfFileProcessor scanDataImportParseMetaInfFileProcessor
                    = mock(ScanDataImportParseMetaInfFileProcessor.class);
            ScanDataImportOverAllStatusProcessor scanDataImportOverAllStatusProcessor
                    = mock(ScanDataImportOverAllStatusProcessor.class);
            ScanDataImportPublishImportScanEventProcessor scanDataImportPublishProcessor
                    = mock(ScanDataImportPublishImportScanEventProcessor.class);
            ScanDataImportPersistScanDataProcessor scanDataImportPersistScanDataProcessor
                    = mock(ScanDataImportPersistScanDataProcessor.class);
            ScanDataImportPersistFilePathsProcessor scanDataImportPersistFilePathsProcessor
                    = mock(ScanDataImportPersistFilePathsProcessor.class);

            return new ScanDataImportParseMetaInfFileRouteBuilder(scanDataImportParseMetaInfFileProcessor,
                    scanDataImportPersistFilePathsProcessor, scanDataImportPublishProcessor,
                    scanDataImportOverAllStatusProcessor, scanDataImportPersistScanDataProcessor);
        }

        public static String getMetaInfJson() throws JSONException {
            JSONObject json = new JSONObject();
            JSONArray filesArr = new JSONArray();

            JSONObject fileOj = new JSONObject();
            fileOj.put("fileName", "topicListing.json");
            fileOj.put("dataEntityType", "topicListing");
            filesArr.put(fileOj);

            json.put("messagingServiceId", "messagingServiceId");
            json.put("scheduleId", "scheduleId");
            json.put("scanId", "scanId");
            json.put("files", filesArr);
            return json.toString();
        }
    }
}
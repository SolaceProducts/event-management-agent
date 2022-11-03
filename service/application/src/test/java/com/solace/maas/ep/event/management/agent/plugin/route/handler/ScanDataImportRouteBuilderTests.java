package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportFileProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanLogsImportLogEventsProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanLogsImportProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.ScanDataImportRouteBuilder;
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
import org.apache.camel.util.IOHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.mockito.Mockito.mock;


@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class ScanDataImportRouteBuilderTests {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:result")
    private MockEndpoint mockResult;

    private static byte[] getZippedText(String entryName) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("TEXT".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        try {
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            IOHelper.copy(byteArrayInputStream, zipOutputStream);
        } finally {
            IOHelper.close(byteArrayInputStream, zipOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Test
    @SneakyThrows
    public void testMockManualImportRoute() throws Exception {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingService");
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, "scheduleId");
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scan1");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "queueListing");

        exchange.getIn().setBody(getZippedText("file"));

        AdviceWith.adviceWith(camelContext, "manualImport",
                route -> {
                    route.replaceFromWith("direct:manualImport");
                    route.weaveAddLast().to("mock:direct:result");
                });

        mockResult.expectedMessageCount(1);
        producerTemplate.send("direct:manualImport", exchange);
        mockResult.assertIsSatisfied();
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            ScanDataImportFileProcessor scanDataImportFileProcessor =
                    mock(ScanDataImportFileProcessor.class);
            ScanLogsImportLogEventsProcessor scanLogsImportLogEventsProcessor =
                    mock(ScanLogsImportLogEventsProcessor.class);
            ScanLogsImportProcessor scanLogsImportProcessor =
                    mock(ScanLogsImportProcessor.class);

            return new ScanDataImportRouteBuilder(scanDataImportFileProcessor,
                    scanLogsImportProcessor,
                    scanLogsImportLogEventsProcessor);
        }
    }
}

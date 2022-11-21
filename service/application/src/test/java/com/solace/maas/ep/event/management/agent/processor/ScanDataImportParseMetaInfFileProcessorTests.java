package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataImportParseMetaInfFileProcessorTests {

    @Autowired
    CamelContext camelContext;

    @InjectMocks
    ScanDataImportParseMetaInfFileProcessor scanDataImportParseMetaInfFileProcessor;


    @SneakyThrows
    @Test
    public void testScanDataImportParseMetaInfFileProcessor() {

        MetaInfFileBO metaInfFileBO = MetaInfFileBO.builder()
                .files(List.of(MetaInfFileDetailsBO.builder().build()))
                .messagingServiceId("messagingServiceId")
                .scheduleId("scheduleId")
                .scanId("scanId")
                .build();

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, "scheduleId");
        exchange.getIn().setBody(metaInfFileBO);

        scanDataImportParseMetaInfFileProcessor.process(exchange);

        assertThatNoException();
    }
}

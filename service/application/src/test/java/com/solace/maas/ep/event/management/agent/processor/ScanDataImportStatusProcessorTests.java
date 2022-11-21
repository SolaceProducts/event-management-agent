package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataImportStatusProcessorTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    CamelContext camelContext;

    @InjectMocks
    ScanDataImportStatusProcessor scanDataImportStatusProcessor;


    @SneakyThrows
    @Test
    public void testScanDataImportStatusProcessor() {

        MetaInfFileDetailsBO metaInfFileDetailsBO = MetaInfFileDetailsBO.builder()
                .fileName("subscriptionConfiguration.json")
                .dataEntityType("subscriptionConfiguration")
                .build();

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(metaInfFileDetailsBO);

        scanDataImportStatusProcessor.process(exchange);

        assertThatNoException();

        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());
        scanDataImportStatusProcessor.process(exchange);

        exception.expect(Exception.class);
    }
}

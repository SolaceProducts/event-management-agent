package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientsPathEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientStoreRepository;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class EmptyScanEntityProcessorTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    CamelContext camelContext;

    @Spy
    @InjectMocks
    EmptyScanEntityProcessorImpl emptyScanEntityProcessor;

    @Mock
    private ScanRecipientStoreRepository scanRecipientStoreRepository;

    @SneakyThrows
    @Test
    public void testEmptyScanEntityProcessor() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "topicListing");

        exchange.getIn().setBody(new ArrayList<>());

        ScanRecipientsPathEntity scanRecipientsPathEntity =
                ScanRecipientsPathEntity.builder()
                        .scan(ScanEntity.builder().id("scanId").build())
                        .path("consumerGroups,consumerGroupConfiguration")
                        .build();

        when(scanRecipientStoreRepository.findScanRecipientPathEntitiesByScanId("scanId"))
                .thenReturn(List.of(scanRecipientsPathEntity));

        emptyScanEntityProcessor.process(exchange);

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testEmptyScanEntityProcessorException() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "topicListing");

        exchange.getIn().setBody(new ArrayList<>());

        ScanRecipientsPathEntity scanRecipientsPathEntity =
                ScanRecipientsPathEntity.builder()
                        .scan(ScanEntity.builder().id("scanId").build())
                        .path("consumerGroups,consumerGroupConfiguration")
                        .build();

        when(scanRecipientStoreRepository.findScanRecipientPathEntitiesByScanId("scanId"))
                .thenReturn(List.of(scanRecipientsPathEntity));

        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());
        emptyScanEntityProcessor.process(exchange);

        exception.expect(Exception.class);
    }
}

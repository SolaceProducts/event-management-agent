package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientHierarchyEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientHierarchyRepository;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    private ScanRecipientHierarchyRepository scanRecipientHierarchyRepository;

    @Mock
    private ProducerTemplate producerTemplate;

    @SneakyThrows
    @Test
    public void testEmptyScanEntityProcessor() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "topicListing");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");

        exchange.getIn().setBody(new ArrayList<>());

        Map<String, String> store = new LinkedHashMap<>();
        store.put("0", "clusterConfiguration,brokerConfiguration");
        store.put("1", "topicListing,topicConfiguration");
        store.put("2", "topicListing,overrideTopicConfiguration");
        store.put("3", "consumerGroups,consumerGroupConfiguration");

        ScanRecipientHierarchyEntity scanRecipientHierarchyEntity =
                ScanRecipientHierarchyEntity.builder()
                        .scanId("scanId")
                        .store(store)
                        .build();

        List<ScanRecipientHierarchyEntity> result = List.of(scanRecipientHierarchyEntity);

        when(scanRecipientHierarchyRepository.findScanRecipientHierarchyEntitiesByScanId("scanId"))
                .thenReturn(result);

        emptyScanEntityProcessor.process(exchange);

        assertThatNoException();

        verify(emptyScanEntityProcessor, times(1))
                .sendCompleteStatusForScanType("scanId", "messagingServiceId", "topicListing");
        verify(emptyScanEntityProcessor, times(1))
                .sendCompleteStatusForScanType("scanId", "messagingServiceId", "topicConfiguration");
        verify(emptyScanEntityProcessor, times(1))
                .sendCompleteStatusForScanType("scanId", "messagingServiceId", "overrideTopicConfiguration");


        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());
        emptyScanEntityProcessor.process(exchange);

        exception.expect(Exception.class);
    }
}

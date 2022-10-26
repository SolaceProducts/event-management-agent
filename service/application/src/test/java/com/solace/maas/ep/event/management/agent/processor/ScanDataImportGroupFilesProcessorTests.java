package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.util.SendImportData;
import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataImportGroupFilesProcessorTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    ManualImportRepository manualImportRepository;

    @Autowired
    CamelContext camelContext;

    @Autowired
    IDGenerator idGenerator;

    @InjectMocks
    ScanDataImportGroupFilesProcessor scanDataImportGroupFilesProcessor;

    @Mock
    SendImportData sendImportData;

    @SneakyThrows
    @Test
    public void testImportGroupFiles() {
        String groupId = UUID.randomUUID().toString();

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, null);
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "scanType");
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");
        exchange.getIn().setHeader("CamelFileName", "data/" + groupId + "/" +
                idGenerator.generateRandomUniqueId() + "/topicListing.json");

        exchange.getIn().setBody("test exchange");

        when(manualImportRepository.save(any(ManualImportEntity.class)))
                .thenReturn(ManualImportEntity.builder().build());

        scanDataImportGroupFilesProcessor.process(exchange);

        assertThatNoException();

        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());
        scanDataImportGroupFilesProcessor.process(exchange);

        exception.expect(Exception.class);
    }
}
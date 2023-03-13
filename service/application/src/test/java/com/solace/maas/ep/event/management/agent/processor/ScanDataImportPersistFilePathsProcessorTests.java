package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import com.solace.maas.ep.event.management.agent.service.ManualImportDetailsService;
import com.solace.maas.ep.event.management.agent.service.ManualImportFilesService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataImportPersistFilePathsProcessorTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    CamelContext camelContext;

    @InjectMocks
    ScanDataImportPersistFilePathsProcessor scanDataImportPersistFilePathsProcessor;

    @Mock
    IDGenerator idGenerator;

    @Mock
    ManualImportDetailsService manualImportDetailsService;

    @Mock
    ManualImportFilesService manualImportFilesService;

    @SneakyThrows
    @Test
    public void testScanDataImportPersistFilePathsProcessor() {
        List<MetaInfFileDetailsBO> metaInfFileBO = List.of(MetaInfFileDetailsBO.builder()
                .fileName("topicListing.json")
                .dataEntityType("topicListing")
                .build());

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(metaInfFileBO);

        when(idGenerator.generateRandomUniqueId())
                .thenReturn("abc123");
        when(manualImportDetailsService.save(any())).thenReturn(null);
        when(manualImportFilesService.saveAll(any())).thenReturn(null);

        scanDataImportPersistFilePathsProcessor.process(exchange);

        assertThatNoException();

        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());
        scanDataImportPersistFilePathsProcessor.process(exchange);

        exception.expect(Exception.class);
    }
}

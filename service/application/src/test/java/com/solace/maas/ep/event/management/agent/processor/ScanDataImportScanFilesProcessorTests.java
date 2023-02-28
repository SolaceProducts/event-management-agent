package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.service.ManualImportService;
import com.solace.maas.ep.event.management.agent.service.ScanTypeService;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataImportScanFilesProcessorTests {

    @Autowired
    CamelContext camelContext;

    @Mock
    IDGenerator idGenerator;

    @Mock
    ScanTypeService scanTypeService;

    @Mock
    ManualImportService manualImportService;

    @InjectMocks
    ScanDataImportPersistScanFilesProcessor scanDataImportFileProcessor;

    @SneakyThrows
    @Test
    public void testScanDataImportScanFilesProcessor() {
        String groupId = UUID.randomUUID().toString();
        String scanId = "sanId";
        String scanType = "subscriptionConfiguration";
        String fileName = "data/" + groupId + "/" + scanId + "/" + scanType + ".json";

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "scanType");
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");
        exchange.getIn().setHeader("CamelFileName", fileName);
        exchange.getIn().setBody("test exchange");

        when(idGenerator.generateRandomUniqueId())
                .thenReturn("abc123");

        when(scanTypeService.findByNameAndScanId(any(String.class), any(String.class)))
                .thenReturn(Optional.ofNullable(ScanTypeEntity.builder()
                        .status(ScanStatusEntity.builder()
                                .status(ScanStatus.IN_PROGRESS.name())
                                .build())
                        .build()));

        when(manualImportService.save(any(ManualImportEntity.class)))
                .thenReturn(ManualImportEntity.builder().build());

        scanDataImportFileProcessor.process(exchange);

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testScanDataImportScanFilesProcessorException() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> scanDataImportFileProcessor.process(exchange));

        assertTrue(thrown.getMessage().contains("Can't apply Scan Status to Scan that doesn't exist!"));
    }

    @SneakyThrows
    @Test
    public void testScanDataImportScanFilesProcessorWithNoScanType() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "scanType");
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, "groupId");
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");
        exchange.getIn().setHeader("CamelFileName", "fileName");
        exchange.getIn().setBody("test exchange");

        when(scanTypeService.findByNameAndScanId(any(String.class), any(String.class)))
                .thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> scanDataImportFileProcessor.process(exchange));

        assertTrue(thrown.getMessage().contentEquals("Can't apply Scan Status to Scan that doesn't exist!"));
    }
}
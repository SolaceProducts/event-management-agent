package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.ScanService;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataImportPersistScanDataProcessorTests {

    @Autowired
    CamelContext camelContext;

    @Mock
    MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @Mock
    ScanService scanService;

    @Mock
    ScanTypeService scanTypeService;

    @Mock
    IDGenerator idGenerator;

    @InjectMocks
    ScanDataImportPersistScanDataProcessor scanDataImportPersistScanDataProcessor;

    @SneakyThrows
    @Test
    public void testScanDataImportPersistScanDataProcessor() {
        String groupId = UUID.randomUUID().toString();
        String scanId = "sanId";
        String emaId = "emaId";
        String scanType = "queueConfiguration,queueListing,subscriptionConfiguration";


//        messagingServiceId "d8i99kj5qzj"
//        emaId "5lcf7wa5jpu"
//        scheduleId "07a075f4-29a3-4387-8fb5-8644c8fff5d3"
//        scanId "xtfz7xyewma"

        List<MetaInfFileDetailsBO> fileDetailsBOS = List.of(
                MetaInfFileDetailsBO.builder()
                        .fileName("queueConfiguration.json")
                        .dataEntityType("queueConfiguration").build(),
                MetaInfFileDetailsBO.builder()
                        .fileName("queueListing.json")
                        .dataEntityType("queueListing").build(),
                MetaInfFileDetailsBO.builder()
                        .fileName("subscriptionConfiguration.json")
                        .dataEntityType("subscriptionConfiguration").build());

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
        exchange.getIn().setHeader(RouteConstants.EVENT_MANAGEMENT_ID, emaId);
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");

        exchange.getIn().setBody(fileDetailsBOS);

        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id("messagingServiceId")
                .name("staging service")
                .type(MessagingServiceType.SOLACE.name())
                .connections(List.of())
                .build();

        when(messagingServiceDelegateService.getMessagingServiceById("messagingServiceId"))
                .thenReturn(messagingServiceEntity);

        when(idGenerator.generateRandomUniqueId())
                .thenReturn("abc123");

        when(scanTypeService.saveAll(any(List.class)))
                .thenReturn(List.of(ScanTypeEntity.builder().build()));

        when(scanService.save(any(ScanEntity.class)))
                .thenReturn(ScanEntity.builder()
                        .id(scanId)
                        .emaId("emaId")
                        .messagingService(messagingServiceEntity)
                        .createdAt(Instant.now())
                        .build());

        scanDataImportPersistScanDataProcessor.process(exchange);

        assertThatNoException();
    }
}

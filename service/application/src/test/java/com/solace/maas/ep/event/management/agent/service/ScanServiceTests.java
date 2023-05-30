package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.logging.StreamingAppender;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundleHierarchyStore;
import com.solace.maas.ep.event.management.agent.processor.RouteCompleteProcessorImpl;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientHierarchyEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientHierarchyRepository;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRepository;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanTypeRepository;
import com.solace.maas.ep.event.management.agent.service.logging.LoggingService;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("CPD-START")
public class ScanServiceTests {
    private final String routeId = UUID.randomUUID().toString();

    @Mock
    LoggingService loggingService;

    @Mock
    StreamingAppender streamingAppender;

    @Mock
    private ScanRepository scanRepository;

    @Mock
    private ScanTypeRepository scanTypeRepository;

    @Mock
    private ScanRecipientHierarchyRepository scanRecipientHierarchyRepository;

    @Mock
    private RouteService routeService;

    @Mock
    private ProducerTemplate producerTemplate;

    @Mock
    private ScanRouteService scanRouteService;

    @Mock
    private IDGenerator idGenerator;

    @Produce
    private ProducerTemplate template;

    @InjectMocks
    private ScanService scanService;

    @Test
    @SneakyThrows
    public void testSingleScanWithRouteBundle() {
        RouteBundle consumerGroupsConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("consumerGroupsConfiguration")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle consumerGroups = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("consumerGroups")
                .destinations(List.of())
                .recipients(List.of(consumerGroupsConfiguration))
                .build();

        RouteBundle routeBundleDestination = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("log:deadend")
                .scanType("none")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle topicConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("topicConfiguration")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle overrideTopicConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("overrideTopicConfiguration")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle topicListing = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("topicListing")
                .destinations(List.of(routeBundleDestination))
                .recipients(List.of(topicConfiguration, overrideTopicConfiguration))
                .build();

        RouteBundle additionalConsumerGroupConfigPart1 = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("additionalConsumerGroupConfigPart1")
                .destinations(List.of(routeBundleDestination))
                .recipients(List.of())
                .build();

        RouteBundle additionalConsumerGroupConfigPart2 = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("additionalConsumerGroupConfigPart2")
                .destinations(List.of(routeBundleDestination))
                .recipients(List.of())
                .build();

        RouteBundle additionalConsumerGroupConfigBundle = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType("consumerGroupsConfiguration")
                .destinations(List.of(routeBundleDestination))
                .recipients(List.of(additionalConsumerGroupConfigPart1, additionalConsumerGroupConfigPart2))
                .build();

        RouteEntity returnedEntity = RouteEntity.builder()
                .id(routeId)
                .childRouteIds("")
                .active(true)
                .build();

        ScanEntity scanEntity = ScanEntity.builder()
                .id(UUID.randomUUID().toString())
                .route(List.of(returnedEntity))
                .build();

        ScanTypeEntity scanType = ScanTypeEntity.builder()
                .id("scan1")
                .name("scanType")
                .scan(scanEntity)
                .build();

        when(idGenerator.generateRandomUniqueId())
                .thenReturn("abc123");
        when(routeService.findById(any(String.class)))
                .thenReturn(Optional.of(returnedEntity));
        when(scanRouteService.saveDestinations(any(List.class)))
                .thenReturn(List.of("log:deadend", List.of()));
        when(scanRepository.save(any(ScanEntity.class)))
                .thenReturn(scanEntity);
        when(scanRepository.findById(any(String.class)))
                .thenReturn(Optional.of(scanEntity));
        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(scanRepository.save(scanEntity))
                .thenReturn(scanEntity);
        when(scanTypeRepository.save(scanType))
                .thenReturn(scanType);
        when(scanRecipientHierarchyRepository.save(any(ScanRecipientHierarchyEntity.class)))
                .thenReturn(mock(ScanRecipientHierarchyEntity.class));

        scanService.singleScan(List.of(topicListing, consumerGroups, additionalConsumerGroupConfigBundle),
                "groupId", "scanId", mock(MessagingServiceEntity.class), "runtimeAgent1");

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testScan() {
        Exchange exchange = ExchangeBuilder.anExchange(mock(ExtendedCamelContext.class)).build();

        when(producerTemplate.send(any(String.class), any(Processor.class)))
                .thenReturn(exchange);

        scanService.scan("group1", "scan1", RouteEntity.builder().build(), "service1");

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testListScans() {
        ScanEntity result = ScanEntity.builder()
                .id("id1")
                .createdAt(Instant.now())
                .messagingService(MessagingServiceEntity.builder()
                        .id("service1")
                        .name("service1")
                        .type("SOLACE")
                        .build())
                .build();

        when(scanRepository.findAll())
                .thenReturn(List.of(result));

        scanService.listScans();

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testFindAll() {
        ScanEntity result = ScanEntity.builder()
                .id("id1")
                .createdAt(Instant.now())
                .messagingService(MessagingServiceEntity.builder()
                        .id("service1")
                        .name("service1")
                        .type("SOLACE")
                        .build())
                .scanTypes(List.of(
                        ScanTypeEntity.builder()
                                .name("SOLACE_ALL")
                                .status(ScanStatusEntity.builder()
                                        .id("status1")
                                        .status("COMPLETE")
                                        .build())
                                .build())
                ).build();

        Pageable pageable = PageRequest.of(0, 1);

        Page<ScanEntity> results = new PageImpl<>(List.of(result), pageable, 1);

        when(scanRepository.findAll(pageable))
                .thenReturn(results);

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testFindByMessagingServiceId() {
        ScanEntity result = ScanEntity.builder()
                .id("id1")
                .createdAt(Instant.now())
                .messagingService(MessagingServiceEntity.builder()
                        .id("service1")
                        .name("service1")
                        .type("SOLACE")
                        .build())
                .scanTypes(List.of(
                        ScanTypeEntity.builder()
                                .name("SOLACE_ALL")
                                .status(ScanStatusEntity.builder()
                                        .id("status1")
                                        .status("COMPLETE")
                                        .build())
                                .build())
                ).build();

        Pageable pageable = PageRequest.of(0, 1);

        Page<ScanEntity> results = new PageImpl<>(List.of(result), pageable, 1);

        when(scanRepository.findAllByMessagingServiceId("service1", pageable))
                .thenReturn(results);

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testParseRouteRecipients() {
        RouteBundle brokerConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("kafkaBrokerConfiguration")
                .scanType("brokerConfiguration")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle clusterConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("kafkaClusterConfiguration")
                .scanType("clusterConfiguration")
                .destinations(List.of())
                .recipients(List.of(brokerConfiguration))
                .build();

        RouteBundle topicConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("kafkaTopicConfiguration")
                .scanType("topicConfiguration")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle overrideTopicConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("kafkaOverrideTopicConfiguration")
                .scanType("overrideTopicConfiguration")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle topicListing = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("kafkaDataPublisher")
                .scanType("topicListing")
                .destinations(List.of())
                .recipients(List.of(topicConfiguration, overrideTopicConfiguration))
                .build();

        RouteBundle consumerGroupConfiguration = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("kafkaConsumerGroupConfiguration")
                .scanType("consumerGroupConfiguration")
                .destinations(List.of())
                .recipients(List.of())
                .build();

        RouteBundle consumerGroups = RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("kafkaConsumerGroupDataPublisher")
                .scanType("consumerGroups")
                .destinations(List.of())
                .recipients(List.of(consumerGroupConfiguration))
                .build();

        RouteBundleHierarchyStore routeBundleHierarchyStore =
                scanService.parseRouteRecipients(List.of(clusterConfiguration, topicListing, consumerGroups),
                        new RouteBundleHierarchyStore());

        List<String> routeBundlerHierarchySore = new ArrayList<>(routeBundleHierarchyStore.getStore().values());

        Assertions.assertEquals(4, routeBundlerHierarchySore.size());
        Assertions.assertTrue(routeBundlerHierarchySore.contains("clusterConfiguration,brokerConfiguration"));
        Assertions.assertTrue(routeBundlerHierarchySore.contains("topicListing,topicConfiguration"));
        Assertions.assertTrue(routeBundlerHierarchySore.contains("topicListing,overrideTopicConfiguration"));
        Assertions.assertTrue(routeBundlerHierarchySore.contains("consumerGroups,consumerGroupConfiguration"));
    }

    @Test
    @SneakyThrows
    public void testSendScanStatus() {
        ScanService service = new ScanService(mock(ScanRepository.class), mock(ScanRecipientHierarchyRepository.class),
                mock(ScanTypeRepository.class),
                mock(ScanRouteService.class), mock(RouteService.class), template, idGenerator);
        service.sendScanStatus("scanId", "groupId", "messagingServiceId",
                "queueListing", ScanStatus.IN_PROGRESS);

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testFindById() {
        when(scanRepository.findById(any(String.class)))
                .thenReturn(Optional.of(ScanEntity.builder().build()));

        scanService.findById("scan1");

        assertThatNoException();
    }
}

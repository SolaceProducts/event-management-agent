package com.solace.maas.ep.event.management.agent.scanManager;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.delegate.KafkaRouteDelegateImpl;
import com.solace.maas.ep.event.management.agent.plugin.localstorage.route.delegate.DataCollectionFileWriterDelegateImpl;
import com.solace.maas.ep.event.management.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanManagerTest {

    @Mock
    EventPortalProperties eventPortalProperties;

    @Mock
    MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @Mock
    private ScanService scanService;

    @InjectMocks
    ScanManager scanManager;

    @Test
    @SneakyThrows
    public void testScanManagerExceptions() {
        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id("id")
                .name("name")
                .type("TEST_SERVICE")
                .connections(List.of())
                .build();

        when(messagingServiceDelegateService.getMessagingServiceById("id"))
                .thenReturn(messagingServiceEntity);

        when(scanService.singleScan(List.of(), "groupId", "scanId",
                mock(MessagingServiceEntity.class), "runtimeAgent1")).thenReturn(Mockito.anyString());

        ScanRequestBO scanRequestBO =
                new ScanRequestBO("id", "scanId", List.of("topics"), List.of());

        Assertions.assertThrows(NullPointerException.class, () -> scanManager.scan(scanRequestBO));

        ScanRequestBO scanRequestBOTopics =
                new ScanRequestBO("id", "scanId", List.of("TEST_SCAN_1"), List.of());
        Assertions.assertThrows(NullPointerException.class, () -> scanManager.scan(scanRequestBO));

        ScanRequestBO scanRequestBOConsumerGroups =
                new ScanRequestBO("id", "scanId", List.of("TEST_SCAN_2"), List.of());
        Assertions.assertThrows(NullPointerException.class, () -> scanManager.scan(scanRequestBO));
    }

    @Test
    @SneakyThrows
    public void testScanManager() {
        String messagingServiceId = "messagingServiceId";
        ScanRequestBO scanRequestBO =
                new ScanRequestBO(messagingServiceId, "scanId", List.of("KAFKA_ALL"), List.of("FILE_WRITER"));

        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id(messagingServiceId)
                .name("name")
                .type("kafka")
                .connections(List.of())
                .build();

        KafkaRouteDelegateImpl scanDelegate =
                mock(KafkaRouteDelegateImpl.class);

        DataCollectionFileWriterDelegateImpl dataCollectionFileWriterDelegate =
                mock(DataCollectionFileWriterDelegateImpl.class);

        when(messagingServiceDelegateService.getMessagingServiceById(messagingServiceId))
                .thenReturn(messagingServiceEntity);

        List<RouteBundle> destinations = getFileWriterDestination(messagingServiceId);
        List<RouteBundle> routes = getKafkaRoutes(destinations, messagingServiceId);

        try (MockedStatic<PluginLoader> pluginLoaderMockedStatic = Mockito.mockStatic(PluginLoader.class)) {
            pluginLoaderMockedStatic.when(() -> PluginLoader.findPlugin("kafka"))
                    .thenReturn(scanDelegate);
            pluginLoaderMockedStatic.when(() -> PluginLoader.findPlugin("FILE_WRITER"))
                    .thenReturn(dataCollectionFileWriterDelegate);
            when(dataCollectionFileWriterDelegate.generateRouteList(
                    List.of(),
                    List.of(),
                    scanRequestBO.getScanTypes().stream().findFirst().orElseThrow(),
                    messagingServiceId))
                    .thenReturn(destinations);
            when(scanDelegate.generateRouteList(destinations, List.of(), "KAFKA_ALL", messagingServiceId))
                    .thenReturn(routes);
            when(scanService.singleScan(List.of(), "groupId", "scanId", mock(MessagingServiceEntity.class),
                    "runtimeAgent1"))
                    .thenReturn(Mockito.anyString());

            scanManager.scan(scanRequestBO);

            assertThatNoException();
        }
    }

    private List<RouteBundle> getFileWriterDestination(String messagingServiceId) {
        return List.of(RouteBundle.builder()
                .destinations(List.of())
                .recipients(List.of())
                .messagingServiceId(messagingServiceId)
                .routeId("seda:dataCollectionFileWrite")
                .scanType("KAFKA_ALL")
                .firstRouteInChain(false)
                .build());
    }

    private List<RouteBundle> getKafkaRoutes(List<RouteBundle> destinations, String messagingServiceId) {
        RouteBundle brokerConfiguration = RouteBundle.builder()
                .destinations(destinations)
                .recipients(List.of())
                .messagingServiceId(messagingServiceId)
                .routeId("kafkaBrokerConfiguration")
                .scanType("brokerConfiguration")
                .firstRouteInChain(false)
                .build();

        RouteBundle topicConfiguration = RouteBundle.builder()
                .destinations(destinations)
                .recipients(List.of())
                .messagingServiceId(messagingServiceId)
                .routeId("kafkaTopicConfiguration")
                .scanType("topicConfiguration")
                .firstRouteInChain(false)
                .build();

        RouteBundle overrideTopicConfiguration = RouteBundle.builder()
                .destinations(destinations)
                .recipients(List.of())
                .messagingServiceId(messagingServiceId)
                .routeId("kafkaOverrideTopicConfiguration")
                .scanType("overrideTopicConfiguration")
                .firstRouteInChain(false)
                .build();

        RouteBundle consumerGroupConfiguration = RouteBundle.builder()
                .destinations(destinations)
                .recipients(List.of())
                .messagingServiceId(messagingServiceId)
                .routeId("kafkaConsumerGroupConfiguration")
                .scanType("consumerGroupConfiguration")
                .firstRouteInChain(false)
                .build();

        return List.of(
                RouteBundle.builder()
                        .destinations(destinations)
                        .recipients(List.of(brokerConfiguration))
                        .messagingServiceId(messagingServiceId)
                        .routeId("kafkaClusterConfiguration")
                        .scanType("clusterConfiguration")
                        .firstRouteInChain(true)
                        .build(),

                RouteBundle.builder()
                        .destinations(destinations)
                        .recipients(List.of(topicConfiguration, overrideTopicConfiguration))
                        .messagingServiceId(messagingServiceId)
                        .routeId("kafkaDataPublisher")
                        .scanType("topicListing")
                        .firstRouteInChain(true)
                        .build(),

                RouteBundle.builder()
                        .destinations(destinations)
                        .recipients(List.of(consumerGroupConfiguration))
                        .messagingServiceId(messagingServiceId)
                        .routeId("kafkaConsumerGroupDataPublisher")
                        .scanType("consumerGroups")
                        .firstRouteInChain(true)
                        .build()
        );
    }
}

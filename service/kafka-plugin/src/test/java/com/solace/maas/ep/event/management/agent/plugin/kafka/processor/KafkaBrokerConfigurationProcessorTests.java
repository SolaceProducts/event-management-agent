package com.solace.maas.ep.event.management.agent.plugin.kafka.processor;

import com.solace.maas.ep.event.management.agent.plugin.KafkaTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.cluster.KafkaBrokerConfigurationProcessor;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.cluster.KafkaBrokerConfigurationEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.cluster.KafkaClusterConfigurationEvent;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.ConfigResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KafkaTestConfig.class)
class KafkaBrokerConfigurationProcessorTests {

    @Mock
    MessagingServiceDelegateService messagingServiceDelegateService;

    @Mock
    KafkaClientConfig kafkaClientConfig;

    private KafkaBrokerConfigurationProcessor kafkaBrokerConfigurationProcessor;

    @BeforeEach
    void mockSetup() {
        KafkaClientConnection kafkaClientConnection = mock(KafkaClientConnection.class);
        KafkaClientReconnection kafkaClientReconnection = mock(KafkaClientReconnection.class);

        KafkaClientConnectionConfig kafkaClientConnectionConfigTimeout = mock(KafkaClientConnectionConfig.class);
        KafkaClientConnectionConfig kafkaClientConnectionConfigMaxIdle = mock(KafkaClientConnectionConfig.class);
        KafkaClientConnectionConfig kafkaClientConnectionConfigRequestTimeout = mock(KafkaClientConnectionConfig.class);

        KafkaClientReconnectionConfig kafkaClientReconnectionConfigBackoff = mock(KafkaClientReconnectionConfig.class);
        KafkaClientReconnectionConfig kafkaClientReconnectionConfigBackoffMax = mock(KafkaClientReconnectionConfig.class);

        when(kafkaClientConfig.getConnections()).thenReturn(kafkaClientConnection);
        when(kafkaClientConfig.getReconnections()).thenReturn(kafkaClientReconnection);

        when(kafkaClientConnection.getTimeout()).thenReturn(kafkaClientConnectionConfigTimeout);
        when(kafkaClientConnectionConfigTimeout.getValue()).thenReturn(60_000);
        when(kafkaClientConnectionConfigTimeout.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientConnection.getMaxIdle()).thenReturn(kafkaClientConnectionConfigMaxIdle);
        when(kafkaClientConnectionConfigMaxIdle.getValue()).thenReturn(10_000);
        when(kafkaClientConnectionConfigMaxIdle.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientConnection.getRequestTimeout()).thenReturn(kafkaClientConnectionConfigRequestTimeout);
        when(kafkaClientConnectionConfigRequestTimeout.getValue()).thenReturn(5_000);
        when(kafkaClientConnectionConfigRequestTimeout.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientReconnection.getBackoff()).thenReturn(kafkaClientReconnectionConfigBackoff);
        when(kafkaClientReconnectionConfigBackoff.getValue()).thenReturn(50);
        when(kafkaClientReconnectionConfigBackoff.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientReconnection.getMaxBackoff()).thenReturn(kafkaClientReconnectionConfigBackoffMax);
        when(kafkaClientReconnectionConfigBackoffMax.getValue()).thenReturn(1000);
        when(kafkaClientReconnectionConfigBackoffMax.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        kafkaBrokerConfigurationProcessor = new KafkaBrokerConfigurationProcessor(messagingServiceDelegateService, kafkaClientConfig);
    }

    @SneakyThrows
    @Test
    void testHandleEvent() {
        List<KafkaClusterConfigurationEvent> body = List.of(
                KafkaClusterConfigurationEvent.builder()
                        .id("0")
                        .host("localhost")
                        .rack("rack1")
                        .port(9090)
                        .build(),
                KafkaClusterConfigurationEvent.builder()
                        .id("1")
                        .host("localhost")
                        .rack("rack2")
                        .port(9092)
                        .build()
        );

        List<ConfigEntry> entries = new ArrayList<>();
        entries.add(new ConfigEntry("A1", "value1"));
        entries.add(new ConfigEntry("B1", "value2"));

        ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, "configResource1");
        Config config = new Config(entries);

        AdminClient adminClient = mock(AdminClient.class);
        DescribeConfigsResult describeConfigsResult = mock(DescribeConfigsResult.class);
        KafkaFuture<Map<ConfigResource, Config>> future = mock(KafkaFuture.class);

        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(adminClient);
        when(adminClient.describeConfigs(anyCollection()))
                .thenReturn(describeConfigsResult);
        when(describeConfigsResult.all())
                .thenReturn(future);
        when(future.get(60_000, TimeUnit.MILLISECONDS))
                .thenReturn(Map.of(configResource, config));

        List<KafkaBrokerConfigurationEvent> kafkaBrokerConfigurationEvents =
                kafkaBrokerConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), body);

        KafkaBrokerConfigurationEvent kafkaBrokerConfigurationEvent = kafkaBrokerConfigurationEvents.stream().findFirst().orElseThrow();
        assertThat(kafkaBrokerConfigurationEvents).hasSize(1);
        assertThat(kafkaBrokerConfigurationEvent.getName()).isEqualTo("configResource1");
        assertThat(kafkaBrokerConfigurationEvent.getConfigurations()).hasSize(2);

        assertThat(kafkaBrokerConfigurationEvent.getConfigurations().get(0).getName()).isEqualTo("A1");
        assertThat(kafkaBrokerConfigurationEvent.getConfigurations().get(1).getName()).isEqualTo("B1");

        assertThatNoException();
    }
}

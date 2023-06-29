package com.solace.maas.ep.event.management.agent.plugin.kafka.processor;

import com.solace.maas.ep.event.management.agent.plugin.KafkaTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.topic.KafkaTopicConfigurationEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.topic.KafkaTopicEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.topic.KafkaTopicConfigurationProcessor;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.acl.AclOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KafkaTestConfig.class)
@SuppressWarnings("PMD")
class KafkaTopicConfigurationProcessorTests {

    @Mock
    private MessagingServiceDelegateService messagingServiceDelegateService;

    @Mock
    private KafkaClientConfig kafkaClientConfig;

    private KafkaTopicConfigurationProcessor kafkaTopicConfigurationProcessor;

    @BeforeEach
    void setupMocks() {
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

        kafkaTopicConfigurationProcessor = new
                KafkaTopicConfigurationProcessor(messagingServiceDelegateService, kafkaClientConfig);
    }

    @Test
    void testHandleEvents() throws Exception {
        Node node = new Node(0, "host1", 9090);
        TopicDescription topic1 = new TopicDescription(
                "topic1",
                false,
                List.of(new TopicPartitionInfo(0, node, List.of(node), List.of(node))),
                Set.of(AclOperation.CREATE),
                Uuid.randomUuid());

        AdminClient adminClient = mock(AdminClient.class);
        DescribeTopicsResult describeTopicsResult = mock(DescribeTopicsResult.class);
        KafkaFuture<Map<String, TopicDescription>> future = mock(KafkaFuture.class);

        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(adminClient);
        when(adminClient.describeTopics(any(List.class)))
                .thenReturn(describeTopicsResult);
        when(describeTopicsResult.all()).thenReturn(future);
        when(future.get(60_000, TimeUnit.MILLISECONDS))
                .thenReturn(Map.of("0", topic1));

        List<KafkaTopicConfigurationEvent> kafkaTopicConfigurationEvents =
                kafkaTopicConfigurationProcessor.handleEvent(
                        Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"),
                        List.of(
                                KafkaTopicEvent.builder()
                                        .name("topic1")
                                        .topicId("id1")
                                        .internal(false)
                                        .build())
                );

        KafkaTopicConfigurationEvent kafkaTopicConfigurationEvent = kafkaTopicConfigurationEvents.get(0);

        assertThat(kafkaTopicConfigurationEvents).hasSize(1);
        assertThat(kafkaTopicConfigurationEvent.getTopicId()).isNotNull();
        assertThat(kafkaTopicConfigurationEvent.getName()).isEqualTo("topic1");
        assertThat(kafkaTopicConfigurationEvent.getAcls()).hasSize(1);
        assertThat(kafkaTopicConfigurationEvent.getAcls().get(0).getName()).isEqualTo("CREATE");
        assertThat(kafkaTopicConfigurationEvent.getPartitions()).hasSize(1);
        assertThat(kafkaTopicConfigurationEvent.getPartitions().get(0).getReplicas()).hasSize(1);
        assertThat(kafkaTopicConfigurationEvent.getPartitions().get(0).getIsr()).hasSize(1);
        assertThat(kafkaTopicConfigurationEvent.getPartitions().get(0).getLeader().getHost()).isEqualTo("host1");

        List<KafkaTopicConfigurationEvent> empty =
                kafkaTopicConfigurationProcessor.handleEvent(
                        Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"),
                        List.of());

        assertThat(empty).isEmpty();

        assertThatNoException();
    }
}

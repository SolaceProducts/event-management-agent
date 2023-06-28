package com.solace.maas.ep.event.management.agent.plugin.kafka.processor;

import com.solace.maas.ep.event.management.agent.plugin.KafkaTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.consumer.KafkaConsumerGroupConfigurationProcessor;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.consumer.KafkaConsumerGroupEvent;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.MemberAssignment;
import org.apache.kafka.clients.admin.MemberDescription;
import org.apache.kafka.common.ConsumerGroupState;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.acl.AclOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KafkaTestConfig.class)
class KafkaConsumerGroupConfigurationProcessorTests {

    @Mock
    private MessagingServiceDelegateService messagingServiceDelegateService;

    @Mock
    private KafkaClientConfig kafkaClientConfig;

    private KafkaConsumerGroupConfigurationProcessor kafkaConsumerGroupConfigurationProcessor;

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

        kafkaConsumerGroupConfigurationProcessor = new
                KafkaConsumerGroupConfigurationProcessor(messagingServiceDelegateService, kafkaClientConfig);
    }

    @SneakyThrows
    @Test
    void testHandleEvent() {
        AdminClient adminClient = mock(AdminClient.class);
        DescribeConsumerGroupsResult describeConsumerGroupsResult = mock(DescribeConsumerGroupsResult.class);
        KafkaFuture<Map<String, ConsumerGroupDescription>> future = mock(KafkaFuture.class);

        MemberDescription members =
                new MemberDescription("memberId", "clientId", "host",
                        new MemberAssignment(Set.of(new TopicPartition("topic", 1))));

        Map<String, ConsumerGroupDescription> ConsumerGroupDescriptionMap =
                Map.of("consumerId", new ConsumerGroupDescription(
                        "groupId",
                        true,
                        List.of(members),
                        "assignorTest",
                        ConsumerGroupState.STABLE,
                        new Node(1, "host", 1),
                        Set.of(AclOperation.ALL)
                ));

        when(messagingServiceDelegateService.getMessagingServiceClient("messagingServiceId"))
                .thenReturn(adminClient);
        when(adminClient.describeConsumerGroups(Stream.of("id").collect(Collectors.toSet())))
                .thenReturn(describeConsumerGroupsResult);
        when(describeConsumerGroupsResult.all())
                .thenReturn(future);
        when(future.get(60_000, TimeUnit.MILLISECONDS))
                .thenReturn((ConsumerGroupDescriptionMap));

        kafkaConsumerGroupConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId"),
                Collections.singletonList(
                        KafkaConsumerGroupEvent
                                .builder()
                                .groupId("id")
                                .simpleConsumerGroup(true)
                                .state("UNKNOWN")
                                .build()));

        adminClient.close();

        assertThatNoException();
    }
}

package com.solace.maas.ep.runtime.agent.plugin.kafka.route.processor;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.consumer.KafkaConsumerGroupConfigurationProcessor;
import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.consumer.KafkaConsumerGroupEvent;
import com.solace.maas.ep.runtime.agent.plugin.service.MessagingServiceDelegateService;
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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KafkaConsumerGroupConfigurationProcessorTests {

    @Mock
    private MessagingServiceDelegateService messagingServiceDelegateService;

    @InjectMocks
    private KafkaConsumerGroupConfigurationProcessor kafkaConsumerGroupConfigurationProcessor;

    @SneakyThrows
    @Test
    public void testHandleEvent() {
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
        when(future.get(30, TimeUnit.SECONDS))
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

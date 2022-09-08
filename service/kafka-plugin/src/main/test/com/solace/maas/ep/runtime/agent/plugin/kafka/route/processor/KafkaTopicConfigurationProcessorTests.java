package com.solace.maas.ep.runtime.agent.plugin.kafka.route.processor;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.topic.KafkaTopicEvent;
import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.topic.KafkaTopicConfigurationProcessor;
import com.solace.maas.ep.runtime.agent.plugin.service.MessagingServiceDelegateService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("PMD")
public class KafkaTopicConfigurationProcessorTests {
    @Mock
    private MessagingServiceDelegateService messagingServiceDelegateService;

    @InjectMocks
    private KafkaTopicConfigurationProcessor kafkaTopicConfigurationProcessor;

    @Test
    public void testHandleEvents() throws Exception {
        AdminClient adminClient = mock(AdminClient.class);
        DescribeTopicsResult describeTopicsResult = mock(DescribeTopicsResult.class);
        KafkaFuture<Map<String, TopicDescription>> future = mock(KafkaFuture.class);

        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(adminClient);
        when(adminClient.describeTopics(any(List.class)))
                .thenReturn(describeTopicsResult);
        when(describeTopicsResult.all()).thenReturn(future);
        when(future.get(30, TimeUnit.SECONDS))
                .thenReturn(Map.of());

        kafkaTopicConfigurationProcessor.handleEvent(
                Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"),
                List.of(
                        KafkaTopicEvent.builder()
                                .name("topic1")
                                .topicId("id1")
                                .internal(false)
                                .build()
                )
        );

        assertThatNoException();
    }
}

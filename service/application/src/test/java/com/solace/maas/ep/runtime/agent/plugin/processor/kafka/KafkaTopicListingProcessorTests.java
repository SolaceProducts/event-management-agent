package com.solace.maas.ep.runtime.agent.plugin.processor.kafka;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.topic.KafkaTopicListingProcessor;
import com.solace.maas.ep.runtime.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Uuid;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class KafkaTopicListingProcessorTests {
    @Mock
    private MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @InjectMocks
    private KafkaTopicListingProcessor kafkaTopicListingProcessor;

    @SneakyThrows
    @Test
    public void testHandleEvent() {
        AdminClient adminClient = mock(AdminClient.class);
        ListTopicsResult listTopicsResult = mock(ListTopicsResult.class);
        KafkaFuture<Collection<TopicListing>> future = mock(KafkaFuture.class);

        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(adminClient);
        when(adminClient.listTopics()).thenReturn(listTopicsResult);
        when(listTopicsResult.listings()).thenReturn(future);
        when(future.get(30, TimeUnit.SECONDS))
                .thenReturn(List.of(new TopicListing("name",
                        new Uuid(0L, 1L), true)));

        kafkaTopicListingProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), null);

        adminClient.close();

        assertThatNoException();
    }
}

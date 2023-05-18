package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.SolaceTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.event.SolaceQueueNameEvent;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SolaceTestConfig.class)
@SuppressWarnings("PMD")
public class SolaceQueueListingProcessorTests {
    @Mock
    private MessagingServiceDelegateService messagingServiceDelegateService;

    @InjectMocks
    private SolaceQueueListingProcessor solaceQueueListingProcessor;

    @SneakyThrows
    @Test
    public void testHandleEvent() {
        SolaceHttpSemp sempClient = mock(SolaceHttpSemp.class);

        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(sempClient);
        when(sempClient.getQueueNames()).thenReturn(List.of(
                Map.of("queueName", "myQueue1"), Map.of("queueName", "myQueue2")));

        List<SolaceQueueNameEvent> queueEventList = solaceQueueListingProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), null);

        assertThat(queueEventList, hasSize(2));
        assertThat(queueEventList, containsInAnyOrder(
                SolaceQueueNameEvent.builder().name("myQueue1").build(),
                SolaceQueueNameEvent.builder().name("myQueue2").build()));
        assertThatNoException();
    }
}

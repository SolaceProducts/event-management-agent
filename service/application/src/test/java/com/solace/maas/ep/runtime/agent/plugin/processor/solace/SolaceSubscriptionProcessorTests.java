package com.solace.maas.ep.runtime.agent.plugin.processor.solace;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.event.SolaceQueueNameEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.event.SolaceSubscriptionEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.semp.SolaceHttpSemp;
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
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class SolaceSubscriptionProcessorTests {
    @Mock
    private MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @InjectMocks
    private SolaceSubscriptionProcessor solaceSubscriptionProcessor;

    @SneakyThrows
    @Test
    public void testHandleEvent() {
        SolaceHttpSemp sempClient = mock(SolaceHttpSemp.class);

        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(sempClient);
        when(sempClient.getSubscriptionForQueue("abc")).thenReturn(List.of(
                Map.of("queueName", "myQueue1", "subscriptionTopic", "sc/topic/1", "msgVpnName", "myMsgVpn"),
                Map.of("queueName", "myQueue1", "subscriptionTopic", "sc/topic/2", "msgVpnName", "myMsgVpn")));

        List<SolaceSubscriptionEvent> subscriptionEvents =
                solaceSubscriptionProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"),
                List.of(SolaceQueueNameEvent.builder().name("abc").build()));

        assertThat(subscriptionEvents, hasSize(1));
        assertThat(subscriptionEvents.get(0).getSubscriptionList(), hasSize(2));
        assertThat(subscriptionEvents.get(0).getSubscriptionList(), containsInAnyOrder("sc/topic/1", "sc/topic/2"));
        assertThatNoException();
    }
}

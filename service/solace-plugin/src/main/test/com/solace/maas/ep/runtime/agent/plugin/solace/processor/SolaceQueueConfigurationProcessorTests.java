package com.solace.maas.ep.runtime.agent.plugin.solace.processor;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.runtime.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("PMD")
public class SolaceQueueConfigurationProcessorTests {
    @Mock
    private MessagingServiceDelegateService messagingServiceDelegateService;

    @InjectMocks
    private SolaceQueueConfigurationProcessor solaceQueueConfigurationProcessor;

    @SneakyThrows
    @Test
    public void testHandleEvent() {
        SolaceHttpSemp sempClient = mock(SolaceHttpSemp.class);

        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(sempClient);
        Map<String, Object> queue1Config = Map.of("queueName", "myQueue1", "accessType", "exclusive");
        Map<String, Object> queue2Config = Map.of("queueName", "myQueue2", "accessType", "non-exclusive");

        when(sempClient.getQueues()).thenReturn(List.of(queue1Config, queue2Config));

        List<Map<String, Object>> queueEventList = solaceQueueConfigurationProcessor.handleEvent(
                Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), null);

        assertThat(queueEventList, hasSize(2));
        assertThat(queueEventList, containsInAnyOrder(queue1Config, queue2Config));
    }
}

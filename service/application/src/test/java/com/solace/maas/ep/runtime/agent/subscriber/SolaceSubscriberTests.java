package com.solace.maas.ep.runtime.agent.subscriber;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.messaging.DirectMessageReceiverBuilder;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.DirectMessageReceiver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Slf4j
public class SolaceSubscriberTests {

    @Mock
    MessagingService messagingService;

    @Mock
    DirectMessageReceiver directMessageReceiver;

    @Mock
    DirectMessageReceiverBuilder directMessageReceiverBuilder;

    @Mock
    ScanCommandMessageHandler scanCommandMessageHandler;

    @Test
    public void testSubscriber() {
        when(messagingService.createDirectMessageReceiverBuilder()).thenReturn(directMessageReceiverBuilder);
        when(directMessageReceiverBuilder.withSubscriptions(ArgumentMatchers.any())).thenReturn(directMessageReceiverBuilder);
        when(directMessageReceiverBuilder.build()).thenReturn(directMessageReceiver);
        when(directMessageReceiver.start()).thenReturn(directMessageReceiver);
        when(scanCommandMessageHandler.getTopicString()).thenReturn("topic");
        SolaceSubscriber solaceSubscriber = new SolaceSubscriber(messagingService);
        solaceSubscriber.registerMessageHandler(scanCommandMessageHandler);
    }
}

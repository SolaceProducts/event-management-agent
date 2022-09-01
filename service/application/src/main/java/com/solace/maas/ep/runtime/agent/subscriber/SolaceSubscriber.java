package com.solace.maas.ep.runtime.agent.subscriber;

import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.resources.TopicSubscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class SolaceSubscriber {

    private final MessagingService messagingService;

    public SolaceSubscriber(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    public void registerMessageHandler(SolaceMessageHandler solaceMessageHandler) {
        DirectMessageReceiver directMessageReceiver = messagingService
                .createDirectMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of(solaceMessageHandler.getTopicString()))
                .build()
                .start();

        directMessageReceiver.receiveAsync(solaceMessageHandler);
        log.debug("Registered message handler for topic {}", solaceMessageHandler.getTopicString());
    }

}

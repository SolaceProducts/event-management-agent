package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.resources.Queue;
import com.solace.messaging.resources.TopicSubscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class SolaceSubscriber {

    @Value("${event-portal.solace-managed-agent}")
    private Boolean solaceManagedAgent;
    private final MessagingService messagingService;

    public SolaceSubscriber(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    public void registerMessageHandler(SolaceMessageHandler solaceMessageHandler) {
        if (solaceManagedAgent) {
            log.info("Cloud managed EMA. Registering persistent message handler");
            registerWithPersistentMessageReceiver(solaceMessageHandler);
        } else {
            log.info("Un managed EMA. Registering persistent message handler");
            registerWithDirectMessageReceiver(solaceMessageHandler);
        }
    }

    private void registerWithDirectMessageReceiver(SolaceMessageHandler solaceMessageHandler) {
        DirectMessageReceiver directMessageReceiver = messagingService
                .createDirectMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of(solaceMessageHandler.getTopicString()))
                .build()
                .start();
        directMessageReceiver.receiveAsync(solaceMessageHandler);
        log.debug("Registered direct message handler for topic {}", solaceMessageHandler.getTopicString());
    }

    private void registerWithPersistentMessageReceiver(SolaceMessageHandler solaceMessageHandler) {
        PersistentMessageReceiver persistentMessageReceiver = messagingService
                .createPersistentMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of(solaceMessageHandler.getTopicString()))
                .build(Queue.durableExclusiveQueue("some_queue"))
                .start();
        persistentMessageReceiver.receiveAsync(solaceMessageHandler);
        log.debug("Registered persistent message handler for topic {}", solaceMessageHandler.getTopicString());
    }

}

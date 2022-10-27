package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncRoutePublisherImpl;
import com.solace.messaging.MessagingService;
import com.solace.messaging.PubSubPlusClientException;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.MessageReceiver;
import com.solace.messaging.resources.TopicSubscription;
import com.solace.messaging.util.CompletionListener;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class SolaceSubscriberNoThread {
    private int count = 0;
    private final MessagingService service;
    private final String subscription;
    private final int maxMessages;

    private final AsyncRoutePublisherImpl publisher;
    private final Exchange exchange;

    private DirectMessageReceiver receiver;
    private MessageReceiver.InboundMessageSupplier nullSupplier;

    @Builder
    public SolaceSubscriberNoThread(MessagingService service, String subscription, int maxMessages, AsyncRoutePublisherImpl publisher,
                                    Exchange exchange) {
        this.service = service;
        this.subscription = subscription;
        this.maxMessages = maxMessages;
        this.publisher = publisher;
        this.exchange = exchange;
    }

    public void consumerMessages() {

        receiver = service
                .createDirectMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of(subscription))
                .build()
                .start();

        // more message supplier are available
        //nullSupplier = MessageReceiver.InboundMessageSupplier.nullMessageSupplier();

        final MessageReceiver.MessageHandler messageHandler = (message) -> {
            // do something with a message, i.e access raw payload:
            publisher.sendMesage(message.getDestinationName(), exchange);
            //byte[] bytes = message.getPayloadAsBytes();
        };
        receiver.receiveAsync(messageHandler);

//        final CompletionListener<DirectMessageReceiver> receiverStartupListener = (directReceiver, throwable) -> {
//            if (throwable == null) {
//                // deal with an exception during start
//            } else {
//                //started successfully, i.e can receive messages
//            }
//        };
//            //receive next 1000 messages
//        while (count < maxMessages && !end) {
//            try {
//                InboundMessage message = receiver.receiveOrElse(nullSupplier);
//                if (message != null) {
//                    String rawTopic = message.getDestinationName();
//                    publisher.sendMesage(rawTopic, exchange);
////                    topicMatcher.getMatchTopicList().add(rawTopic);
////                    topicMatcher.match(rawTopic, "/");
//
//                    // Turn on to get message size
//                    // log.info("Topic {}  payload size {}", rawTopic, message.getPayloadAsString().length());
//                    count++;
//                }
//            } catch (PubSubPlusClientException e) {
//                // deal with an exception, mostly timeout exception
//            }
//        }
//        Future<Void> done = receiver.terminateAsync(10000);
//        try {
//            done.get();
//        } catch (InterruptedException e) {
//            //throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            //throw new RuntimeException(e);
//        }
//        return count;
    }

    public void stop() {
        receiver.terminate(10000);
    }

//    public int getCount(){
//        return count;
//    }
//
//    @Override
//    public Integer call() {
//        return consumeMessagesWithMaxCount();
//    }
}

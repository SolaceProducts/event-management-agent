package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncRoutePublisherImpl;
import com.solace.messaging.MessagingService;
import com.solace.messaging.PubSubPlusClientException;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.MessageReceiver;
import com.solace.messaging.resources.TopicSubscription;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class SolaceTopicCollector implements Callable<Integer> {
    private int count = 0;
    private boolean end = false;
    private final MessagingService service;
    private final String subscription;
    private final int maxMessages;

    private final AsyncRoutePublisherImpl publisher;
    private final Exchange exchange;

    @Builder
    public SolaceTopicCollector(MessagingService service, String subscription, int maxMessages, AsyncRoutePublisherImpl publisher,
                                Exchange exchange) {
        this.service = service;
        this.subscription = subscription;
        this.maxMessages = maxMessages;
        this.publisher = publisher;
        this.exchange = exchange;
    }

    public int consumeMessagesWithMaxCount() {

        final DirectMessageReceiver receiver = service
                .createDirectMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of(subscription))
                .build()
                .start();

        // more message supplier are available
        final MessageReceiver.InboundMessageSupplier nullSupplier = MessageReceiver.InboundMessageSupplier.nullMessageSupplier();
        //receive next 1000 messages
        while (count < maxMessages && !end) {
            try {
                InboundMessage message = receiver.receiveOrElse(nullSupplier);
                if (message != null) {
                    String rawTopic = message.getDestinationName();
                    publisher.sendMesage(rawTopic, exchange);
//                    topicMatcher.getMatchTopicList().add(rawTopic);
//                    topicMatcher.match(rawTopic, "/");

                    // Turn on to get message size
                    // log.info("Topic {}  payload size {}", rawTopic, message.getPayloadAsString().length());
                    count++;
                }
            } catch (PubSubPlusClientException e) {
                // deal with an exception, mostly timeout exception
            }
        }
        Future<Void> done = receiver.terminateAsync(10000);
        try {
            done.get();
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        } catch (ExecutionException e) {
            //throw new RuntimeException(e);
        }
        return count;
    }

    public void stop() {
        end = true;
    }

    public int getCount(){
        return count;
    }

    @Override
    public Integer call() {
        return consumeMessagesWithMaxCount();
    }
}

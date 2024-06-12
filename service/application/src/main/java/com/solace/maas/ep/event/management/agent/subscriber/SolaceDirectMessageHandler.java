package com.solace.maas.ep.event.management.agent.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.MessageReceiver;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class SolaceDirectMessageHandler<T extends MOPMessage> extends BaseSolaceMessageHandler implements MessageReceiver.MessageHandler {
    private final Map<String, Class> cachedJSONDecoders = new HashMap();
    private final String topicString;

    protected SolaceDirectMessageHandler(String topicString, SolaceSubscriber solaceSubscriber) {
        super();
        this.topicString = topicString;
        solaceSubscriber.registerMessageHandler(this);
    }

    public String getTopicString() {
        return topicString;
    }

    @Override
    public void onMessage(InboundMessage inboundMessage) {
        String messageAsString = inboundMessage.getPayloadAsString();
        String mopMessageSubclass = inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER);

        try {
            Class messageClass = cachedJSONDecoders.get(mopMessageSubclass);
            T message;

            if (messageClass == null) {
                messageClass = Class.forName(mopMessageSubclass);
                cachedJSONDecoders.put(mopMessageSubclass, messageClass);
            }

            String receivedClassName = messageClass.getSimpleName();

            setupMDC(messageAsString, receivedClassName);

            message = (T) toMessage(messageAsString, messageClass);
            log.trace("onMessage: {}\n{}", messageClass, messageAsString);
            log.trace("onMessage: {} {}", inboundMessage.getDestinationName(), message);
            receiveMessage(inboundMessage.getDestinationName(), message);
        } catch (ClassNotFoundException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public abstract void receiveMessage(String destinationName, T message);
}

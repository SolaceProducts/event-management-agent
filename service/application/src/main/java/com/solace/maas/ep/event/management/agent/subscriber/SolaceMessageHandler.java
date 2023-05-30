package com.solace.maas.ep.event.management.agent.subscriber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.solace.maas.ep.event.management.agent.plugin.mop.EnumDeserializer;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.MessageReceiver;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class SolaceMessageHandler<T extends MOPMessage> implements MessageReceiver.MessageHandler {

    private final String topicString;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final SimpleModule module = new SimpleModule();
    private final Map<String, Class> cachedJSONDecoders = new HashMap();

    static {
        // Register all the enums we expect to cross versions and allow them to be mapped to null instead of throwing an exception if unknown
        module.addDeserializer(MOPMessageType.class, new EnumDeserializer<>(MOPMessageType.class));
        module.addDeserializer(MOPProtocol.class, new EnumDeserializer<>(MOPProtocol.class));
        module.addDeserializer(MOPSvcType.class, new EnumDeserializer<>(MOPSvcType.class));
        module.addDeserializer(MOPUHFlag.class, new EnumDeserializer<>(MOPUHFlag.class));

        objectMapper.registerModule(module);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public SolaceMessageHandler(String topicString, SolaceSubscriber solaceSubscriber) {
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
            T message = null;

            if (messageClass == null) {
                messageClass = Class.forName(mopMessageSubclass);
                cachedJSONDecoders.put(mopMessageSubclass, messageClass);
            }

            message = (T) objectMapper.readValue(messageAsString, messageClass);
            log.trace("onMessage: {}\n{}", messageClass, messageAsString);
            log.trace("onMessage: {} {}", inboundMessage.getDestinationName(), message);
            receiveMessage(inboundMessage.getDestinationName(), message);
        } catch (ClassNotFoundException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void receiveMessage(String destinationName, T message);
}

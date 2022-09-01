package com.solace.maas.ep.runtime.agent.plugin.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.runtime.agent.plugin.config.EnableRtoCondition;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessage;
import com.solace.messaging.MessagingService;
import com.solace.messaging.PubSubPlusClientException;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.resources.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
@ConditionalOnMissingBean(EnableRtoCondition.class)
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class SolacePublisher {

    private static final ObjectMapper mapper = new ObjectMapper();
    private DirectMessagePublisher directMessagePublisher;
    private final OutboundMessageBuilder outboundMessageBuilder;
    private final MessagingService messagingService;

    public SolacePublisher(DirectMessagePublisher directMessagePublisher,
                           OutboundMessageBuilder outboundMessageBuilder,
                           MessagingService messagingService) {
        this.directMessagePublisher = directMessagePublisher;
        this.outboundMessageBuilder = outboundMessageBuilder;
        this.messagingService = messagingService;
    }

    public void publish(MOPMessage message, String topicString) {
        Topic topic = Topic.of(topicString);

        try {
            String messageString = mapper.writeValueAsString(message);
            Properties properties = getProperties(message);
            OutboundMessage outboundMessage = outboundMessageBuilder
                    .fromProperties(properties)
                    .build(messageString);
            log.trace("publishing to {}:\n{}", topicString, messageString);
            directMessagePublisher.publish(outboundMessage, topic, properties);
        } catch (PubSubPlusClientException e) {
            log.error("PubSubPlus Client Exception while attempting to publish message: {}", message.toString(), e);
        } catch (IllegalStateException e) {
            log.error("Illegal State Exception while attempting to publish message: {}", message.toString(), e);
        } catch (IllegalArgumentException e) {
            log.error("Illegal Argument Exception while attempting to publish message: {}", message.toString(), e);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException while attempting to publish message: {}", message.toString(), e);
        }
    }

    private Properties getProperties(MOPMessage message) {
        Properties properties = new Properties();
        properties.put(MOPConstants.MOP_MSG_META_CONTENT_TYPE, "application/json");
        properties.put(MOPConstants.MOP_MSG_META_DECODER, message.getClass().getCanonicalName());
        properties.put(MOPConstants.MOP_VER, message.getMopVer());
        properties.put(MOPConstants.MOP_PROTOCOL, message.getMopProtocol().toString());
        properties.put(MOPConstants.MOP_MSG_TYPE, message.getMopMsgType().toString());
        return properties;
    }
}

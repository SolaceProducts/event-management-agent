package com.solace.maas.ep.event.management.agent.plugin.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.config.EnableRtoCondition;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.messaging.PubSubPlusClientException;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.publisher.PersistentMessagePublisher;
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
    private final OutboundMessageBuilder outboundMessageBuilder;
    private PersistentMessagePublisher persistentMessagePublisher;

    public SolacePublisher(OutboundMessageBuilder outboundMessageBuilder,
                           PersistentMessagePublisher persistentMessagePublisher) {
        this.outboundMessageBuilder = outboundMessageBuilder;
        this.persistentMessagePublisher = persistentMessagePublisher;
    }

    public void publish(MOPMessage message, String topicString) {
        Topic topic = Topic.of(topicString);

        try {
            String messageString = mapper.writeValueAsString(message);
            synchronized (this) {
                Properties properties = getProperties(message);
                properties.put(SolaceProperties.MessageProperties.PERSISTENT_ACK_IMMEDIATELY, Boolean.TRUE);
                OutboundMessage outboundMessage = outboundMessageBuilder
                        .fromProperties(properties)
                        .build(messageString);
                log.trace("publishing to {}:\n{}", topicString, messageString);
                //persistentMessagePublisher.publishAwaitAcknowledgement(outboundMessage, topic, 2000L);
                persistentMessagePublisher.publish(outboundMessage, topic, 2000L);
            }
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

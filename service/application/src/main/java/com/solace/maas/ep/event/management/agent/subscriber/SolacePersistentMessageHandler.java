package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.MessageProcessor;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.MessageReceiver;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.resources.Queue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:true}== false && ${event-portal.managed:false} == true")
public class SolacePersistentMessageHandler extends BaseSolaceMessageHandler implements MessageReceiver.MessageHandler,
        ApplicationListener<ApplicationReadyEvent> {
    private final Map<String, Class> cachedJSONDecoders = new HashMap();

    private final Map<Class, MessageProcessor> messageProcessorsByClassType;
    private final MessagingService messagingService;
    private final EventPortalProperties eventPortalProperties;
    @Getter
    private PersistentMessageReceiver persistentMessageReceiver;

    protected SolacePersistentMessageHandler(MessagingService messagingService,
                                             EventPortalProperties eventPortalProperties,
                                             List<MessageProcessor> messageProcessorList) {

        super();
        this.messagingService = messagingService;
        this.eventPortalProperties = eventPortalProperties;
        messageProcessorsByClassType = messageProcessorList.stream()
                .collect(Collectors.toMap(MessageProcessor::supportedClass, Function.identity()));

    }

    @Override
    public void onMessage(InboundMessage inboundMessage) {
        String mopMessageSubclass = "";
        MessageProcessor processor = null;
        Object message = null;
        try {
            mopMessageSubclass = inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER);
            String messageAsString = inboundMessage.getPayloadAsString();
            Class messageClass = cachedJSONDecoders.get(mopMessageSubclass);
            if (messageClass == null) {
                messageClass = Class.forName(mopMessageSubclass);
                cachedJSONDecoders.put(mopMessageSubclass, messageClass);
            }
            processor = messageProcessorsByClassType.get(messageClass);
            if (processor == null) {
                throw new UnsupportedOperationException("Could not find message processor for message of class " + messageClass.getCanonicalName());
            }
            setupMDC(messageAsString, messageClass.getSimpleName());
            log.trace("onMessage: {}\n{}", messageClass, messageAsString);
            message = toMessage(messageAsString, messageClass);
            processor.processMessage(processor.castToMessageClass(message));

        } catch (Exception e) {
            if (processor != null && message != null) {
                log.error("Error while processing inbound message from queue for mopMessageSubclass: {}", mopMessageSubclass);
                try {
                    processor.onFailure(e, processor.castToMessageClass(message));
                } catch (Exception e1) {
                    log.error("error while handling message processing failure for mopMessageSubclass: {}", mopMessageSubclass, e);
                }

            } else {
                log.error("Unsupported message and/or processor encountered. Skipping processing", e);
            }

        } finally {
            persistentMessageReceiver.ack(inboundMessage);
        }
    }

    private PersistentMessageReceiver buildPersistentMessageReceiver(Queue queue) {
        return messagingService
                .createPersistentMessageReceiverBuilder()
                .build(queue)
                .start();
    }

    private Queue determineQueue() {
        if (StringUtils.isEmpty(eventPortalProperties.getIncomingRequestQueueName())) {
            throw new IllegalArgumentException(
                    "Cloud managed event management agent requires a non empty value for 'incomingRequestQueueName'. " +
                            "Please check the application.yml file");
        }
        return Queue.durableNonExclusiveQueue(eventPortalProperties.getIncomingRequestQueueName());
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Queue queue = determineQueue();
        persistentMessageReceiver = buildPersistentMessageReceiver(queue);
        persistentMessageReceiver.receiveAsync(this);
        log.info("Binding to queue with persistent message handler");
    }
}

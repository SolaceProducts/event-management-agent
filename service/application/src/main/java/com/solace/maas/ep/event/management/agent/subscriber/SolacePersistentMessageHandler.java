package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.MessageProcessor;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.MessageReceiver;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.resources.Queue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:false}== false && ${event-portal.managed:false} == true")
public class SolacePersistentMessageHandler extends BaseSolaceMessageHandler implements MessageReceiver.MessageHandler {
    private final Map<String, Class> cachedJSONDecoders = new HashMap();

    private final Map<Class, MessageProcessor> messageProcessorsByClassType;
    private final MessagingService messagingService;
    private final EventPortalProperties eventPortalProperties;
    private final PersistentMessageReceiver persistentMessageReceiver;

    protected SolacePersistentMessageHandler(MessagingService messagingService,
                                             EventPortalProperties eventPortalProperties,
                                             List<MessageProcessor> messageProcessorList) {

        super();
        this.messagingService = messagingService;
        this.eventPortalProperties = eventPortalProperties;
        messageProcessorsByClassType = messageProcessorList.stream()
                .collect(Collectors.toMap(MessageProcessor::supportedClass, Function.identity()));
        Queue queue = determineQueue();
        persistentMessageReceiver = getPersistentMessageReceiver(queue);
        persistentMessageReceiver.receiveAsync(this);
        log.debug("Bound to queue {}", queue.getName());
    }


    @Override
    public void onMessage(InboundMessage inboundMessage) {
        String mopMessageSubclass = inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER);
        String messageAsString = inboundMessage.getPayloadAsString();

        try {
            Class messageClass = cachedJSONDecoders.get(mopMessageSubclass);
            if (messageClass == null) {
                messageClass = Class.forName(mopMessageSubclass);
                cachedJSONDecoders.put(mopMessageSubclass, messageClass);
            }
            MessageProcessor processor = messageProcessorsByClassType.get(messageClass);
            if (processor == null) {
                throw new IllegalArgumentException("Could not find message processor for message of class " + messageClass);
            }
            setupMDC(messageAsString, messageClass.getSimpleName());
            log.trace("onMessage: {}\n{}", messageClass, messageAsString);
            processor.processMessage(processor.castToMessageClass(toMessage(messageAsString, messageClass)));
            persistentMessageReceiver.ack(inboundMessage);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    private PersistentMessageReceiver getPersistentMessageReceiver(Queue queue) {
        return messagingService
                .createPersistentMessageReceiverBuilder()
                .build(queue)
                .start();
    }

    private Queue determineQueue() {
        if (StringUtils.isNotEmpty(eventPortalProperties.getIncomingRequestQueueName())) {
            return Queue.durableNonExclusiveQueue(eventPortalProperties.getIncomingRequestQueueName());
        } else {
            return Queue.durableNonExclusiveQueue(
                    "ep_core_ema_requests_" + eventPortalProperties.getRuntimeAgentId()
            );
        }
    }

}

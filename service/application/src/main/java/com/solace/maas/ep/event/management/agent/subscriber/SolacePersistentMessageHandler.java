package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.MessageProcessor;
import com.solace.maas.ep.event.management.agent.util.MdcTaskDecorator;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.MessageReceiver;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.resources.Queue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private final ThreadPoolTaskExecutor executor;
    @Getter
    @SuppressWarnings("PMD.MutableStaticState")
    private PersistentMessageReceiver persistentMessageReceiver;

    // only used for testing
    @Setter
    private SolacePersistentMessageHandlerObserver messageHandlerObserver;

    protected SolacePersistentMessageHandler(MessagingService messagingService,
                                             EventPortalProperties eventPortalProperties,
                                             List<MessageProcessor> messageProcessorList) {

        super();
        this.messagingService = messagingService;
        this.eventPortalProperties = eventPortalProperties;
        messageProcessorsByClassType = messageProcessorList.stream()
                .collect(Collectors.toMap(MessageProcessor::supportedClass, Function.identity()));
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(eventPortalProperties.getCommandThreadPoolMinSize());
        executor.setMaxPoolSize(eventPortalProperties.getCommandThreadPoolMaxSize());
        executor.setQueueCapacity(eventPortalProperties.getCommandThreadPoolQueueSize());
        executor.setThreadNamePrefix("solace-persistent-message-handler-pool-");
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.initialize();
    }


    private boolean notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase phase, InboundMessage inboundMessage) {
        if (messageHandlerObserver != null) {
            return messageHandlerObserver.onPhaseChange(inboundMessage, phase);
        }
        return true;
    }

    @Override
    public void onMessage(InboundMessage inboundMessage) {
        notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase.RECEIVED,inboundMessage);
        executor.submit(() -> processMessage(inboundMessage));
    }


    private void processMessage(InboundMessage inboundMessage) {
        notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase.PROCESSING_INITIATED,inboundMessage);
        String mopMessageSubclass = "";
        MessageProcessor processor = null;
        Object message = null;
        Boolean isFailed = false;
        Instant startTime = Instant.now();
        try {
            mopMessageSubclass = inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER);
            String messageAsString = inboundMessage.getPayloadAsString();
            Class messageClass = cachedJSONDecoders.computeIfAbsent(mopMessageSubclass, this::loadClass);
            processor = messageProcessorsByClassType.get(messageClass);
            if (processor == null) {
                throw new UnsupportedOperationException("Could not find message processor for message of class " + messageClass.getCanonicalName());
            }
            setupMDC(messageAsString, messageClass.getSimpleName());
            log.trace("onMessage: {}\n{}", messageClass, messageAsString);
            message = toMessage(messageAsString, messageClass);
            // for testing purposes, we want to be able to stop processing the message and simulate a restart of the EMA
            if (notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase.PRE_PROCESSOR_EXECUTION,inboundMessage)) {
                processor.processMessage(processor.castToMessageClass(message));
                notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase.PROCESSOR_COMPLETED,inboundMessage);
            }
        } catch (Exception e) {
            isFailed = true;
            handleProcessingError(mopMessageSubclass, processor, message, e);
            notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase.FAILED,inboundMessage);
        } finally {
            if(!isFailed){
                processor.sendCycleTimeMetric(startTime, processor.castToMessageClass(message));
            }
            // for testing purposes, we want to be able to stop processing the message and simulate a failure leading to not acknowledging the message
            if (notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase.PRE_ACKNOWLEDGED,inboundMessage)) {
                acknowledgeMessage(inboundMessage);
            }
            notifyPersistentMessageHandlerObserver(PersistentMessageHandlerObserverPhase.ACKNOWLEDGED,inboundMessage);
        }
    }

    private Class loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("Failed to load class: {}", className, e);
            throw new RuntimeException("Failed to load class: " + className, e);
        }
    }

    private void handleProcessingError(String mopMessageSubclass, MessageProcessor processor, Object message, Exception e) {
        if (processor != null && message != null) {
            log.error("Error while processing inbound message from queue for mopMessageSubclass: {}", mopMessageSubclass, e);
            try {
                processor.onFailure(e, processor.castToMessageClass(message));
            } catch (Exception e1) {
                log.error("Error while handling message processing failure for mopMessageSubclass: {}", mopMessageSubclass, e1);
            }
        } else {
            log.error("Unsupported message and/or processor encountered. Skipping processing", e);
        }
    }

    private void acknowledgeMessage(InboundMessage inboundMessage) {
        synchronized (persistentMessageReceiver) {
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

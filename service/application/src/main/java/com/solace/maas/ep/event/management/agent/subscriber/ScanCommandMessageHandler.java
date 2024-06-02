package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.ScanCommandMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:false}== false && ${event-portal.managed:false} == false")
public class ScanCommandMessageHandler extends SolaceDirectMessageHandler<ScanCommandMessage> {

    private final ScanCommandMessageProcessor scanCommandMessageProcessor;

    public ScanCommandMessageHandler(SolaceConfiguration solaceConfiguration,
                                     SolaceSubscriber solaceSubscriber,
                                     ScanCommandMessageProcessor scanCommandMessageProcessor) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/v1/scanStart/>", solaceSubscriber);
        this.scanCommandMessageProcessor = scanCommandMessageProcessor;
    }

    @Override
    public void receiveMessage(String destinationName, ScanCommandMessage message) {
        MDC.clear();
        log.debug("Received scan command message: {} for event broker: {}, traceId: {}",
                message, message.getMessagingServiceId(), message.getTraceId());
        scanCommandMessageProcessor.processMessage(message);
    }
}

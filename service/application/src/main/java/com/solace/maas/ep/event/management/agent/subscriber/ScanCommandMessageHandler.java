package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.ScanCommandMessageProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ENTITY_TYPE_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_SCAN_EVENT_RECEIVED;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.SCAN_ID_TAG;

@Slf4j
@Component
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:true}== false && ${event-portal.managed:false} == false")
public class ScanCommandMessageHandler extends SolaceDirectMessageHandler<ScanCommandMessage> {

    private final ScanCommandMessageProcessor scanCommandMessageProcessor;
    private final MeterRegistry meterRegistry;

    public ScanCommandMessageHandler(SolaceConfiguration solaceConfiguration,
                                     SolaceSubscriber solaceSubscriber,
                                     ScanCommandMessageProcessor scanCommandMessageProcessor,
                                     MeterRegistry meterRegistry) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/v1/scanStart/>", solaceSubscriber);
        this.scanCommandMessageProcessor = scanCommandMessageProcessor;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void receiveMessage(String destinationName, ScanCommandMessage message) {
        MDC.clear();
        log.debug("Received scan command message: {} for event broker: {}, traceId: {}",
                message, message.getMessagingServiceId(), message.getTraceId());
        meterRegistry.counter(MAAS_EMA_SCAN_EVENT_RECEIVED, ENTITY_TYPE_TAG, message.getType(),
                ORG_ID_TAG, message.getOrgId(), SCAN_ID_TAG, message.getScanId()).increment();
        scanCommandMessageProcessor.processMessage(message);
    }
}

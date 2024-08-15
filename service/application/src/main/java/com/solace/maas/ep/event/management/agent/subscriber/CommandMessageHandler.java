package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.CommandMessageProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import static com.solace.maas.ep.common.metrics.MetricConstants.ENTITY_TYPE_TAG;
import static com.solace.maas.ep.common.metrics.MetricConstants.MAAS_EMA_EVENT_RECEIVED;
import static com.solace.maas.ep.common.metrics.MetricConstants.ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.MetricConstants.STATUS_TAG;

@Slf4j
@Component
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:true}== false && ${event-portal.managed:false} == false")
public class CommandMessageHandler extends SolaceDirectMessageHandler<CommandMessage> {

    private final MeterRegistry meterRegistry;
    private final CommandMessageProcessor commandMessageProcessor;

    public CommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber,
            CommandMessageProcessor commandMessageProcessor,
            MeterRegistry meterRegistry) {
        super(solaceConfiguration.getTopicPrefix() + "command/v1/>", solaceSubscriber);
        this.meterRegistry = meterRegistry;
        this.commandMessageProcessor = commandMessageProcessor;
    }

    @Override
    public void receiveMessage(String destinationName, CommandMessage message) {
        log.debug("receiveMessage {}\n{}", destinationName, message);
        meterRegistry.counter(MAAS_EMA_EVENT_RECEIVED, ENTITY_TYPE_TAG, message.getType(),
                        ORG_ID_TAG, message.getOrgId(), STATUS_TAG, message.getStatus().name()).increment();
        commandMessageProcessor.processMessage(message);
    }
}

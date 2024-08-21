package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.HeartbeatMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This is here for developers to test that messages are flowing without having to test with ep-core.
 * <p>
 * This should normally be disabled.
 */
@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.enableHeartbeats} " +
        "&& ${event-portal.managed:false} == false" +
        "&& ${eventPortal.gateway.messaging.testHeartbeats}")
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class HeartbeatMessageHandler extends SolaceDirectMessageHandler<HeartbeatMessage> {

    private final MeterRegistry meterRegistry;

    public HeartbeatMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber,
            MeterRegistry meterRegistry) {
        super(solaceConfiguration.getTopicPrefix() + "heartbeat/>", solaceSubscriber);
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void receiveMessage(String destinationName, HeartbeatMessage message) {
        log.trace("receiveMessage {}", message);
    }
}

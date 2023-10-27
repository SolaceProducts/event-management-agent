package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.HeartbeatMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
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
        "and ${eventPortal.gateway.messaging.testHeartbeats}")
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class HeartbeatMessageHandler extends SolaceMessageHandler<HeartbeatMessage> {

    public HeartbeatMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber) {
        super(solaceConfiguration.getTopicPrefix() + "heartbeat/>", solaceSubscriber);
    }

    @Override
    public void receiveMessage(String destinationName, HeartbeatMessage message) {
        log.trace("receiveMessage {}", message);
    }
}

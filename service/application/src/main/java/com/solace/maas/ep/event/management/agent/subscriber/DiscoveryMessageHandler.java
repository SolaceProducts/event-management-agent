package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:false}== false && ${event-portal.managed:false} == false")
public class DiscoveryMessageHandler extends SolaceDirectMessageHandler<MOPMessage> {

    public DiscoveryMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber) {
        super(solaceConfiguration.getTopicPrefix() + "discovery/>", solaceSubscriber);
    }

    @Override
    public void receiveMessage(String destinationName, MOPMessage message) {
        log.debug("receiveMessage {}\n{}", destinationName, message);
    }
}

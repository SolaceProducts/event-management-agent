package com.solace.maas.ep.runtime.agent.subscriber;

import com.solace.maas.ep.runtime.agent.config.SolaceConfiguration;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class DiscoveryMessageHandler extends SolaceMessageHandler<MOPMessage> {

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

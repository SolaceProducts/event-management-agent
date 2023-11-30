package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandPublisher {

    private final SolacePublisher solacePublisher;
    private final SolaceConfiguration solaceConfiguration;

    public CommandPublisher(SolacePublisher solacePublisher, SolaceConfiguration solaceConfiguration) {
        this.solacePublisher = solacePublisher;
        this.solaceConfiguration = solaceConfiguration;
    }

    /**
     * Sends the command response to EP.
     * <p>
     * The topic for command response:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/commandResponse/v1/{correlationId}
     */

    public void sendCommandResponse(MOPMessage message, Map<String, String> topicDetails) {

        String topicString =
                String.format("%scommandResponse/v1/%s",
                        solaceConfiguration.getTopicPrefix(),
                        topicDetails.get("correlationId"));

        solacePublisher.publish(message, topicString);
    }
}

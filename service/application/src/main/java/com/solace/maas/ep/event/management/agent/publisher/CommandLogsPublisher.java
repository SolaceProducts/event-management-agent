package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.CommandLogMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandLogsPublisher {

    private final SolacePublisher solacePublisher;
    private final SolaceConfiguration solaceConfiguration;

    public CommandLogsPublisher(SolacePublisher solacePublisher, SolaceConfiguration solaceConfiguration) {

        this.solacePublisher = solacePublisher;
        this.solaceConfiguration = solaceConfiguration;
    }

    public void sendCommandLogData(CommandLogMessage message, Map<String, String> topicDetails) {
        String topicString = solaceConfiguration.getTopicPrefix() +
                String.format("commandLogs/v1/%s", topicDetails.get("commandCorrelationId"));

        solacePublisher.publish(message, topicString);
    }
}

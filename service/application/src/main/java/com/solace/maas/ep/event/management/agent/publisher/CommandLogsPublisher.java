package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.CommandLogMessage;
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

    public CommandLogsPublisher(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    public void sendCommandLogData(CommandLogMessage message, Map<String, String> topicDetails) {
        String topicString = String.format("sc/ep/runtime/%s/%s/command/logs/v1/%s/%s",
                topicDetails.get("orgId"),
                topicDetails.get("runtimeAgentId"),
                topicDetails.get("messagingServiceId"),
                topicDetails.get("commandCorrelationId"));

        solacePublisher.publish(message, topicString);
    }
}

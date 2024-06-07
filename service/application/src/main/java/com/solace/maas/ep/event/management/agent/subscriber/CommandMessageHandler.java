package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.CommandMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:false}== false && ${event-portal.managed:false} == false")
public class CommandMessageHandler extends SolaceDirectMessageHandler<CommandMessage> {

    private final CommandMessageProcessor commandMessageProcessor;

    public CommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber, CommandMessageProcessor commandMessageProcessor) {
        super(solaceConfiguration.getTopicPrefix() + "command/v1/>", solaceSubscriber);
        this.commandMessageProcessor = commandMessageProcessor;
    }

    @Override
    public void receiveMessage(String destinationName, CommandMessage message) {
        log.debug("receiveMessage {}\n{}", destinationName, message);
        commandMessageProcessor.processMessage(message);
    }
}

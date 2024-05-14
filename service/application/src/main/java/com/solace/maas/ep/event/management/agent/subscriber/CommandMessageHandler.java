package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandMessageHandler extends SolaceMessageHandler<CommandMessage> {

    private final CommandManager commandManager;

    public CommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber, CommandManager commandManager) {
        super(solaceConfiguration.getTopicPrefix() + "command/v1/>", solaceSubscriber);
        this.commandManager = commandManager;
    }

    @Override
    public void receiveMessage(String destinationName, CommandMessage message) {
        log.debug("receiveMessage {}\n{}", destinationName, message);
        commandManager.execute(message);
    }

    @Override
    public Class<CommandMessage> supportedMessageClass() {
        return CommandMessage.class;
    }
}

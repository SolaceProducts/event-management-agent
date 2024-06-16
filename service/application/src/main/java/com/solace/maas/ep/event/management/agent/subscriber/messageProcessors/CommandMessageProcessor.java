package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandMessageProcessor implements MessageProcessor<CommandMessage> {

    private final CommandManager commandManager;

    private final DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper;

    public CommandMessageProcessor(CommandManager commandManager,
                                   DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper) {
        this.commandManager = commandManager;
        this.dynamicResourceConfigurationHelper = dynamicResourceConfigurationHelper;
    }

    @Override
    public void processMessage(CommandMessage message) {
        dynamicResourceConfigurationHelper.loadSolaceBrokerResourceConfigurations(message.getResources());
        commandManager.execute(message);
    }

    @Override
    public Class supportedClass() {
        return CommandMessage.class;
    }

    @Override
    public CommandMessage castToMessageClass(Object message) {
        return (CommandMessage) message;
    }

    @Override
    public void onFailure(Exception e, CommandMessage message) {
        commandManager.handleError(e, message);
    }
}

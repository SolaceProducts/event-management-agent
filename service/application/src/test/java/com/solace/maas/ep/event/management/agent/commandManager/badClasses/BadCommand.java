package com.solace.maas.ep.event.management.agent.commandManager.badClasses;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;

@SuppressWarnings("PMD")
public class BadCommand extends Command {
    private CommandTypeBad commandType;

    public void setCommandType(CommandTypeBad commandType) {
        this.commandType = commandType;
    }
}

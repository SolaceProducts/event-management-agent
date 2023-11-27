package com.solace.maas.ep.event.management.agent.plugin.command.model;

public enum CommandType {
    terraform("terraform");

    private final String type;

    CommandType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType();
    }

}

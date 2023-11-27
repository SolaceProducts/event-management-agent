package com.solace.maas.ep.event.management.agent.plugin.command.model;

public enum ExecutionType {
    parallel("parallel"),
    serial("serial");

    private final String type;

    ExecutionType(String type) {
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

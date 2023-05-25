package com.solace.maas.ep.event.management.agent.plugin.task;

public enum TaskState {
    ABSENT("ABSENT"),
    PRESENT("PRESENT"),
    READ("READ");

    public final String label;

    TaskState(String label) {
        this.label = label;
    }
}

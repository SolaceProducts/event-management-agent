package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task;

public enum TaskState {
    ABSENT("ABSENT"),
    PRESENT("PRESENT"),
    READ("READ");

    public final String label;

    private TaskState(String label) {
        this.label = label;
    }
}

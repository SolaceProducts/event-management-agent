package com.solace.maas.ep.event.management.agent.plugin.mop;

public enum MOPUHFlag {
    ignore(0),
    fail(1);

    private final int id;

    MOPUHFlag(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

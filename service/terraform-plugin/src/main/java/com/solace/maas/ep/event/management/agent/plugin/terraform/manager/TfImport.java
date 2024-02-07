package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

public class TfImport {
    public TfImport(String id, String to) {
        this.id = id;
        this.to = to;
    }

    public String id;
    public String to;

    @Override
    public String toString() {
        return "import {\n" +
                "\tto = " + to + '\n' +
                "\tid = \"" + id + "\"\n" +
                '}';
    }
}

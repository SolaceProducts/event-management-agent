package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

public class TerraformImport {
    public TerraformImport(String id, String to) {
        this.id = id;
        this.to = to;
    }

    public final String id;
    public final String to;

    @Override
    public String toString() {
        return "import {\n" +
                "\tto = " + to + '\n' +
                "\tid = \"" + id + "\"\n" +
                '}';
    }
}

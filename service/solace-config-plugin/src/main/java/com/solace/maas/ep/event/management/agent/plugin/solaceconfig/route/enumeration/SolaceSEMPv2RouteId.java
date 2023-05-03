package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

public enum SolaceSempRouteId {
    SOLACE_SEMPv2_COMMAND("solaceSEMPv2CommandDataPublisher");

    public final String label;

    SolaceSempRouteId(String label) {
        this.label = label;
    }
}

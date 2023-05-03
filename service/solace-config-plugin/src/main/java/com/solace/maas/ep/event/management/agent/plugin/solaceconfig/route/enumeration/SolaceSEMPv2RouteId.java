package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

public enum SolaceSEMPv2RouteId {
    SOLACE_SEMPv2_COMMAND("solaceSEMPv2CommandDataPublisher");

    public final String label;

    SolaceSEMPv2RouteId(String label) {
        this.label = label;
    }
}

package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

public enum SolaceRouteType {
    SOLACE_SEMPv2_COMMAND("solaceSEMPv2Command");

    public final String label;

    SolaceRouteType(String label) {
        this.label = label;
    }
}

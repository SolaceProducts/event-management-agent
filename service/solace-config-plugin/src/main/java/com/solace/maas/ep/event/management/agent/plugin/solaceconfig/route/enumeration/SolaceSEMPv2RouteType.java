package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

public enum SolaceSEMPv2RouteType {
    SOLACE_SEMPv2_COMMAND("solaceSEMPv2Command");

    public final String label;

    SolaceSEMPv2RouteType(String label) {
        this.label = label;
    }
}

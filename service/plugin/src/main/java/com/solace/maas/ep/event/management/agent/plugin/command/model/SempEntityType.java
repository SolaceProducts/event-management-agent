package com.solace.maas.ep.event.management.agent.plugin.command.model;

public enum SempEntityType {
    solaceQueue("solaceQueue"),
    solaceAcl("solaceAcl"),
    solaceClientUsername("solaceClientUsername"),
    solaceAuthorizationGroup("solaceAuthorizationGroup"),
    subscriptionTopic("subscriptionTopic");
    private final String value;

    SempEntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

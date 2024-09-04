package com.solace.maas.ep.common.model;

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

    public SempEntityType fromValue(String value) {
        for (SempEntityType entityType : SempEntityType.values()) {
            if (entityType.getValue().equals(value)) {
                return entityType;
            }
        }
        throw new IllegalArgumentException("Unsupported entity type: " + value);
    }
}

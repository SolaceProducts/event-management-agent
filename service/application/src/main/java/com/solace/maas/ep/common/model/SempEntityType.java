package com.solace.maas.ep.common.model;

public enum SempEntityType {
    solaceQueue("solaceQueue"),
    solaceAclProfile("solaceAclProfile"),
    solaceAclSubscribeTopicException("solaceAclSubscribeTopicException"),
    solaceAclPublishTopicException("solaceAclPublishTopicException"),
    solaceClientUsername("solaceClientUsername"),
    solaceAuthorizationGroup("solaceAuthorizationGroup"),
    solaceQueueSubscriptionTopic("solaceQueueSubscriptionTopic");
    private final String value;

    SempEntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SempEntityType fromValue(String value) {
        for (SempEntityType entityType : SempEntityType.values()) {
            if (entityType.getValue().equals(value)) {
                return entityType;
            }
        }
        throw new IllegalArgumentException("Unsupported entity type: " + value);
    }
}

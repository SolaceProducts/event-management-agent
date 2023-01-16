package com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration;

public enum SolaceRouteId {
    SOLACE_QUEUE_LISTING("solaceDataPublisher"),
    SOLACE_QUEUE_CONFIG("solaceQueueConfiguration"),
    SOLACE_SUBSCRIPTION_CONFIG("solaceSubscriptionConfiguration"),
    SOLACE_TOPIC_COLLECTOR("solaceTopicCollector"),
    SOLACE_TOPIC_CLASSIFIER("solaceTopicClassifier");
    public final String label;

    SolaceRouteId(String label) {
        this.label = label;
    }
}

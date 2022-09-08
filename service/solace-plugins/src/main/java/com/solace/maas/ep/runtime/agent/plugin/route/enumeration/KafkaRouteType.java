package com.solace.maas.ep.runtime.agent.plugin.route.enumeration;

public enum KafkaRouteType {
    KAFKA_BROKER_CONFIGURATION("brokerConfiguration"),
    KAFKA_CUSTER_CONFIGURATION("clusterConfiguration"),
    KAFKA_CONSUMER_GROUPS("consumerGroups"),
    KAFKA_CONSUMER_GROUP_CONFIGURATION("consumerGroupConfiguration"),
    KAFKA_TOPIC_LISTING("topicListing"),
    KAFKA_FEATURES("features"),
    KAFKA_OVERRIDE_TOPIC_CONFIGURATION("overrideTopicConfiguration"),
    KAFKA_TOPIC_CONFIGURATION("topicConfiguration"),
    KAFKA_PRODUCERS("topicProducer");

    public final String label;

    KafkaRouteType(String label){
        this.label = label;
    }
}

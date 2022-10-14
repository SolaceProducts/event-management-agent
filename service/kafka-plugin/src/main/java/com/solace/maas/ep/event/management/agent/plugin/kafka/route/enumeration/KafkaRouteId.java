package com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration;

public enum KafkaRouteId {
    KAFKA_BROKER_CONFIGURATION("kafkaBrokerConfiguration"),
    KAFKA_CLUSTER_CONFIGURATION("kafkaClusterConfiguration"),
    KAFKA_TOPIC_LISTING("kafkaDataPublisher"),
    KAFKA_TOPIC_CONFIGURATION("kafkaTopicConfiguration"),
    KAFKA_OVERRIDE_TOPIC_CONFIGURATION("kafkaOverrideTopicConfiguration"),
    KAFKA_PRODUCERS("kafkaTopicProducer"),
    KAFKA_CONSUMER_GROUPS("kafkaConsumerGroupDataPublisher"),
    KAFKA_CONSUMER_GROUP_CONFIGURATION("kafkaConsumerGroupConfiguration"),
    KAFKA_FEATURES("kafkaFeatures");

    public final String label;

    KafkaRouteId(String label){
        this.label = label;
    }
}

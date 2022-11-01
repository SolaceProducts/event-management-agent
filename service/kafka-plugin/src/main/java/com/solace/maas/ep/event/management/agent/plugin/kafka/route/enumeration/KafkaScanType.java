package com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
public enum KafkaScanType {
    KAFKA_TOPIC_LISTING,
    KAFKA_TOPIC_CONFIGURATION,
    KAFKA_TOPIC_CONFIGURATION_FULL,
    KAFKA_TOPIC_OVERRIDE_CONFIGURATION,
    KAFKA_PRODUCERS,
    KAFKA_CONSUMER_GROUPS,
    KAFKA_CONSUMER_GROUPS_CONFIGURATION,
    KAFKA_CLUSTER_CONFIGURATION,
    KAFKA_BROKER_CONFIGURATION,
    KAFKA_FEATURES,
    KAFKA_ALL
}

package com.solace.maas.ep.event.management.agent.config.plugin.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public enum MessagingServiceType {
    @JsonProperty("Solace")
    SOLACE,

    @JsonProperty("Kafka")
    KAFKA,

    @JsonProperty("RabbitMq")
    RABBITMQ,

    @JsonProperty("ConfluentSchemaRegistry")
    CONFLUENT_SCHEMA_REGISTRY
}

package com.solace.maas.ep.event.management.agent.config.plugin.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessagingServiceType {
    @JsonProperty("Solace")
    SOLACE,

    @JsonProperty("Kafka")
    KAFKA,

    @JsonProperty("RabbitMq")
    RABBITMQ
}

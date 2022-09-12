package com.solace.maas.ep.runtime.agent.plugin.config.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessagingServiceType {
    @JsonProperty("Solace")
    SOLACE,

    @JsonProperty("Kafka")
    KAFKA,

    @JsonProperty("RabbitMq")
    RABBITMQ
}

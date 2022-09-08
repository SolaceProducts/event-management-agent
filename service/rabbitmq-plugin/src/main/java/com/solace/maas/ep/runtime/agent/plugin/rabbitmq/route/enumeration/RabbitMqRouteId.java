package com.solace.maas.ep.runtime.agent.plugin.rabbitmq.route.enumeration;

public enum RabbitMqRouteId {
    RABBIT_MQ_QUEUE("rabbitMqQueue");

    public final String label;

    RabbitMqRouteId(String label){
        this.label = label;
    }
}

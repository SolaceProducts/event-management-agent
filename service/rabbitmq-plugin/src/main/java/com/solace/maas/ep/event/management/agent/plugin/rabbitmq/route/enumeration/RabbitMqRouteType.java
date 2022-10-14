package com.solace.maas.ep.event.management.agent.plugin.rabbitmq.route.enumeration;

public enum RabbitMqRouteType {
    RABBIT_MQ_QUEUE("queue");

    public final String label;

    RabbitMqRouteType(String label){
        this.label = label;
    }
}

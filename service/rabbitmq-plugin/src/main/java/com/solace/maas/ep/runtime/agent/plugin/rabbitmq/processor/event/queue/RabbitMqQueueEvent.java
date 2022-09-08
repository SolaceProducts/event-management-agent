package com.solace.maas.ep.runtime.agent.plugin.rabbitmq.processor.event.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RabbitMqQueueEvent implements Serializable {
    private String vhost;

    private String name;

    private Boolean autoDelete;

    private Boolean exclusive;

    private Map<String, Object> arguments;

    private String node;

    private String exclusiveConsumerTag;

    private String state;
}

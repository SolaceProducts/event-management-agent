package com.solace.maas.ep.event.management.agent.plugin.ibmmq.processor.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class IbmMqSubscriptionEvent implements Serializable {

    private static final long serialVersionUID = 7693606299215131178L;

    private String resolvedTopicString;
    private String name;
    private String id;
}
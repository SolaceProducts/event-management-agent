package com.solace.maas.ep.common.model;

import lombok.Data;

import java.util.List;

@Data
public class EventBrokerResourceConfiguration extends ResourceConfiguration {
    private String id;
    private String brokerType;
    private String name;
    private List<EventBrokerConnectionConfiguration> connections;
    private ResourceConfigurationType resourceConfigurationType;
}
package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;

import java.util.ArrayList;
import java.util.List;

public abstract class MessagingServiceConverter {
    @SuppressWarnings("unchecked")
    public <T extends EventProperty, R extends EventProperty> R convertPropertyEntityToEvent(T property) {
        return (R) EventProperty.builder()
                .id(property.getId())
                .name(property.getName())
                .property(property.getProperty())
                .build();
    }

    public <T> List<T> ensureList(List<T> listItems) {
        return listItems == null ? new ArrayList<>() : listItems;
    }
}

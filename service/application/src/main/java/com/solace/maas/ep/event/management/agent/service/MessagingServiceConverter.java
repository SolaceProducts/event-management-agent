package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;

import java.util.ArrayList;
import java.util.List;

public abstract class MessagingServiceConverter {
    @SuppressWarnings("unchecked")
    public <T extends EventProperty> EventProperty convertPropertyEntityToEvent(T property) {
        return EventProperty.builder()
                .id(property.getId())
                .name(property.getName())
                .value(property.getValue())
                .build();
    }

    public <T> List<T> ensureList(List<T> listItems) {
        return listItems == null ? new ArrayList<>() : listItems;
    }
}

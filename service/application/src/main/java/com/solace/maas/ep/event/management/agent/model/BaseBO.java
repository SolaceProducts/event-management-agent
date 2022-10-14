package com.solace.maas.ep.event.management.agent.model;

public interface BaseBO<K> {

    Long getCreatedTime();

    Long getUpdatedTime();

    String getCreatedBy();

    String getUpdatedBy();
}

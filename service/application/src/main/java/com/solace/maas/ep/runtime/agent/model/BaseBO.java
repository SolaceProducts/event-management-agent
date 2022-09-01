package com.solace.maas.ep.runtime.agent.model;

public interface BaseBO<K> {

    Long getCreatedTime();

    Long getUpdatedTime();

    String getCreatedBy();

    String getUpdatedBy();
}

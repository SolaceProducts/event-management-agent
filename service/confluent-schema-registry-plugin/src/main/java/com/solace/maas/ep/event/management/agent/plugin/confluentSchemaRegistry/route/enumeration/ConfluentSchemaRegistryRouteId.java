package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.enumeration;

public enum ConfluentSchemaRegistryRouteId {
    CONFLUENT_SCHEMA_REGISTRY_SCHEMA("confluentSchemaRegistryDataPublisher");

    public final String label;

    ConfluentSchemaRegistryRouteId(String label) {
        this.label = label;
    }
}

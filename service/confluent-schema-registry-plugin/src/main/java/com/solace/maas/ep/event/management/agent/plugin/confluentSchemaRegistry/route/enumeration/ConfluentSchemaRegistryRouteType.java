package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.enumeration;

public enum ConfluentSchemaRegistryRouteType {
    CONFLUENT_SCHEMA_REGISTRY_SCHEMA("schema");

    public final String label;

    ConfluentSchemaRegistryRouteType(String label) {
        this.label = label;
    }
}

package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConfluentSchemaRegistrySchemaEvent implements Serializable {
    private String subject;
    private String version;
    private String id;
    private String references;
    private String schema;
    private Boolean internal;
}

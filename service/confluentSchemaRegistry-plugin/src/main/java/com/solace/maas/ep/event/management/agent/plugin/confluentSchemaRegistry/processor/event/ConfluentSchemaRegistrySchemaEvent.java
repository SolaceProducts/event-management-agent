package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConfluentSchemaRegistrySchemaEvent implements Serializable {
    private String subject;
    private Integer version;
    private Integer id;
    private List<ConfluentSchemaRegistrySchemaReference> references;
    private String schemaType;
    private String schema;
    private Boolean internal;
}

package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResourcePatternEvent implements Serializable {
    private String name;

    private Boolean isUnknown;

    private ResourceTypeEvent type;

    private ResourcePatternTypeEvent patternType;

    private ResourcePatternFilterEvent filter;
}

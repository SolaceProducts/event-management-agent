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
public class ResourcePatternTypeEvent implements Serializable {
    private byte code;
    
    private Boolean isUnknown;
    
    private Integer ordinal;
    
    private Boolean isSpecific;
}

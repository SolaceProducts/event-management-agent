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
public class AclPermissionTypeEvent implements Serializable {
    private Boolean isUnknown;

    private byte code;

    private String name;

    private Integer ordinal;
}

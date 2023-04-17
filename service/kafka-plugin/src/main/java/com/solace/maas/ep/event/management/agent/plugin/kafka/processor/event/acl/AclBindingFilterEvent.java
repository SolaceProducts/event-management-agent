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
public class AclBindingFilterEvent implements Serializable {
    private String findIndefiniteField;

    private Boolean isUnknown;

    private Boolean matchesAtMostOne;

    private AccessControlEntryFilterEvent accessControlEntryFilter;
}

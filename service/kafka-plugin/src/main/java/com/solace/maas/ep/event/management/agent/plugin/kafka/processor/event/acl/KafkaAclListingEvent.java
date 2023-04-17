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
public class KafkaAclListingEvent implements Serializable {
    private Boolean isUnknown;

    private AccessControlEntryEvent accessControlEntry;

    private AclBindingFilterEvent filter;

    private ResourcePatternEvent pattern;
}

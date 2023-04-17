package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general.KafkaAclEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccessControlEntryFilterEvent implements Serializable {
    private String host;

    private Boolean isUnknown;

    private String principal;

    private String findIndefiniteField;

    private KafkaAclEvent aclOperation;

    private AclPermissionTypeEvent permissionType;
}

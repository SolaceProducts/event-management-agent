package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.acl;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.AccessControlEntryEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.AccessControlEntryFilterEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.AclBindingFilterEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.AclPermissionTypeEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.KafkaAclListingEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.ResourcePatternEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.ResourcePatternFilterEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.ResourcePatternTypeEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.acl.ResourceTypeEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general.KafkaAclEvent;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.acl.AccessControlEntry;
import org.apache.kafka.common.acl.AccessControlEntryFilter;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourcePatternFilter;
import org.apache.kafka.common.resource.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Component
@SuppressWarnings("PMD")
public class KafkaAclListingProcessor extends ResultProcessorImpl<List<KafkaAclListingEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public KafkaAclListingProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public List<KafkaAclListingEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        AclBindingFilter filter = new AclBindingFilter(
                new ResourcePatternFilter(ResourceType.ANY, null, PatternType.ANY),
                new AccessControlEntryFilter(null, null, AclOperation.ANY, AclPermissionType.ANY)
        );

        return adminClient.describeAcls(filter)
                .values()
                .get(30, TimeUnit.SECONDS)
                .stream()
                .map(result -> {
                    AccessControlEntryEvent accessControlEntryEvent = createAccessControlEntry(result.entry());
                    AclBindingFilterEvent aclBindingFilterEvent = createAclBindingFilterEvent(result.toFilter());
                    ResourcePatternEvent resourcePatternEvent = createResourcePatternEvent(result.pattern());

                    return KafkaAclListingEvent.builder()
                            .accessControlEntry(accessControlEntryEvent)
                            .filter(aclBindingFilterEvent)
                            .pattern(resourcePatternEvent)
                            .isUnknown(result.isUnknown())
                            .build();
                }).collect(Collectors.toUnmodifiableList());
    }

    private AccessControlEntryEvent createAccessControlEntry(AccessControlEntry accessControlEntry) {
        AclOperation aclOperation = accessControlEntry.operation();

        KafkaAclEvent kafkaAclEvent = createAclOperationEvent(aclOperation);

        return AccessControlEntryEvent.builder()
                .aclOperation(kafkaAclEvent)
                .host(accessControlEntry.host())
                .principal(accessControlEntry.principal())
                .ordinal(aclOperation.ordinal())
                .build();
    }

    private AclBindingFilterEvent createAclBindingFilterEvent(AclBindingFilter aclBindingFilter) {
        AccessControlEntryFilter accessControlEntryFilter = aclBindingFilter.entryFilter();

        AccessControlEntryFilterEvent accessControlEntryFilterEvent =
                createAccessControlEntryFilterEvent(accessControlEntryFilter);

        return AclBindingFilterEvent.builder()
                .accessControlEntryFilter(accessControlEntryFilterEvent)
                .findIndefiniteField(aclBindingFilter.findIndefiniteField())
                .matchesAtMostOne(aclBindingFilter.matchesAtMostOne())
                .isUnknown(aclBindingFilter.isUnknown())
                .build();
    }

    private AccessControlEntryFilterEvent createAccessControlEntryFilterEvent(AccessControlEntryFilter accessControlEntryFilter) {
        AclOperation aclOperation = accessControlEntryFilter.operation();
        AclPermissionType aclPermissionType = accessControlEntryFilter.permissionType();

        KafkaAclEvent kafkaAclEvent = createAclOperationEvent(aclOperation);
        AclPermissionTypeEvent aclPermissionTypeEvent = createAclPermissionTypeEvent(aclPermissionType);

        return AccessControlEntryFilterEvent.builder()
                .host(accessControlEntryFilter.host())
                .isUnknown(accessControlEntryFilter.isUnknown())
                .findIndefiniteField(accessControlEntryFilter.findIndefiniteField())
                .principal(accessControlEntryFilter.principal())
                .aclOperation(kafkaAclEvent)
                .permissionType(aclPermissionTypeEvent)
                .build();
    }

    private AclPermissionTypeEvent createAclPermissionTypeEvent(AclPermissionType aclPermissionType) {
        return AclPermissionTypeEvent.builder()
                .code(aclPermissionType.code())
                .isUnknown(aclPermissionType.isUnknown())
                .name(aclPermissionType.name())
                .ordinal(aclPermissionType.ordinal())
                .build();
    }

    private ResourcePatternEvent createResourcePatternEvent(ResourcePattern resourcePattern) {
        ResourceType resourceType = resourcePattern.resourceType();
        PatternType patternType = resourcePattern.patternType();
        ResourcePatternFilter resourcePatternFilter = resourcePattern.toFilter();

        ResourceTypeEvent resourceTypeEvent = createResourceTypeEvent(resourceType);
        ResourcePatternTypeEvent patternTypeEvent = createResourcePatternTypeEvent(patternType);
        ResourcePatternFilterEvent resourcePatternFilterEvent = createResourcePatternFilterEvent(resourcePatternFilter);

        return ResourcePatternEvent.builder()
                .type(resourceTypeEvent)
                .patternType(patternTypeEvent)
                .filter(resourcePatternFilterEvent)
                .name(resourcePattern.name())
                .isUnknown(resourcePattern.isUnknown())
                .build();
    }

    private ResourcePatternFilterEvent createResourcePatternFilterEvent(ResourcePatternFilter resourcePatternFilter) {
        ResourceType resourceType = resourcePatternFilter.resourceType();
        PatternType patternType = resourcePatternFilter.patternType();

        ResourceTypeEvent resourceTypeEvent = createResourceTypeEvent(resourceType);
        ResourcePatternTypeEvent patternTypeEvent = createResourcePatternTypeEvent(patternType);

        return ResourcePatternFilterEvent.builder()
                .type(resourceTypeEvent)
                .patternType(patternTypeEvent)
                .name(resourcePatternFilter.name())
                .findIndefiniteField(resourcePatternFilter.findIndefiniteField())
                .matchesAtMostOnce(resourcePatternFilter.matchesAtMostOne())
                .isUnknown(resourcePatternFilter.isUnknown())
                .build();
    }

    private ResourceTypeEvent createResourceTypeEvent(ResourceType resourceType) {
        return ResourceTypeEvent.builder()
                .code(resourceType.code())
                .name(resourceType.name())
                .ordinal(resourceType.ordinal())
                .isUnknown(resourceType.isUnknown())
                .build();
    }

    private ResourcePatternTypeEvent createResourcePatternTypeEvent(PatternType patternType) {
        return ResourcePatternTypeEvent.builder()
                .code(patternType.code())
                .isSpecific(patternType.isSpecific())
                .ordinal(patternType.ordinal())
                .isUnknown(patternType.isUnknown())
                .build();
    }

    private KafkaAclEvent createAclOperationEvent(AclOperation aclOperation) {
        return KafkaAclEvent.builder()
                .unknown(aclOperation.isUnknown())
                .name(aclOperation.name())
                .ordinal(aclOperation.ordinal())
                .build();
    }
}

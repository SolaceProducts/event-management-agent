package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.consumer;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.consumer.ConsumerEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.consumer.ConsumerTopicPartitionEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.consumer.KafkaConsumerGroupConfigurationEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.consumer.KafkaConsumerGroupEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general.KafkaAclEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general.KafkaNodeEvent;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.MemberDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaConsumerGroupConfigurationProcessor extends
        ResultProcessorImpl<List<KafkaConsumerGroupConfigurationEvent>, List<KafkaConsumerGroupEvent>> {

    private final MessagingServiceDelegateService messagingServiceDelegateService;
    private final long timeout;
    private final TimeUnit timeUnit;

    @Autowired
    public KafkaConsumerGroupConfigurationProcessor(MessagingServiceDelegateService messagingServiceDelegateService,
                                                    KafkaClientConfig kafkaClientConfig) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        timeout = kafkaClientConfig.getConnections().getTimeout().getValue();
        timeUnit = kafkaClientConfig.getConnections().getTimeout().getUnit();
    }

    private KafkaConsumerGroupEvent mapConsumerGroupEvent(ConsumerGroupDescription consumerGroupDescription) {
        return KafkaConsumerGroupEvent.builder()
                .groupId(consumerGroupDescription.groupId())
                .simpleConsumerGroup(consumerGroupDescription.isSimpleConsumerGroup())
                .state(consumerGroupDescription.state().name())
                .build();
    }

    private List<ConsumerEvent> mapMembers(ConsumerGroupDescription consumerGroupDescription) {
        return consumerGroupDescription
                .members()
                .stream()
                .map(memberDescription -> ConsumerEvent.builder()
                        .consumerId(memberDescription.consumerId())
                        .groupInstanceId(memberDescription.groupInstanceId().orElse("UNKNOWN"))
                        .clientId(memberDescription.clientId())
                        .host(memberDescription.host())
                        .partitions(mapMembersPartitions(memberDescription))
                        .build())
                .sorted(Comparator.comparing(ConsumerEvent::getConsumerId))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<ConsumerTopicPartitionEvent> mapMembersPartitions(MemberDescription memberDescription) {
        return memberDescription
                .assignment()
                .topicPartitions()
                .stream()
                .map(topicPartition ->
                        ConsumerTopicPartitionEvent.builder()
                                .partition(topicPartition.partition())
                                .topic(topicPartition.topic())
                                .build()
                )
                .collect(Collectors.toUnmodifiableList());
    }

    private String mapPartitionAssignor(ConsumerGroupDescription consumerGroupDescription) {
        return consumerGroupDescription.partitionAssignor();
    }

    private KafkaNodeEvent mapCoordinator(ConsumerGroupDescription consumerGroupDescription) {
        return KafkaNodeEvent.builder()
                .id(consumerGroupDescription.coordinator().id())
                .rack(consumerGroupDescription.coordinator().rack())
                .host(consumerGroupDescription.coordinator().host())
                .port(consumerGroupDescription.coordinator().port())
                .build();
    }

    private void mapAcls(ConsumerGroupDescription consumerGroupDescription, List<KafkaAclEvent> acls) {
        List<KafkaAclEvent> mappedAcls = consumerGroupDescription
                .authorizedOperations()
                .stream()
                .map(aclOperation ->
                        KafkaAclEvent.builder()
                                .name(aclOperation.name())
                                .ordinal(aclOperation.ordinal())
                                .unknown(aclOperation.isUnknown())
                                .build())
                .sorted()
                .collect(Collectors.toUnmodifiableList());

        acls.addAll(mappedAcls);
    }

    @Override
    public List<KafkaConsumerGroupConfigurationEvent> handleEvent(Map<String, Object> properties, List<KafkaConsumerGroupEvent> body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        List<KafkaConsumerGroupConfigurationEvent> configurations = new ArrayList<>();

        Set<String> groupIds = body.stream()
                .map(KafkaConsumerGroupEvent::getGroupId)
                .collect(Collectors.toUnmodifiableSet());

        DescribeConsumerGroupsResult describeConsumerGroupsResult = adminClient.describeConsumerGroups(groupIds);

        Map<String, ConsumerGroupDescription> ConsumerGroupDescriptionMap =
                describeConsumerGroupsResult
                        .all()
                        .get(timeout, timeUnit);

        ConsumerGroupDescriptionMap
                .forEach((key, consumerGroupDescription) -> {

                    List<KafkaAclEvent> acls = new ArrayList<>();

                    if (Objects.nonNull(consumerGroupDescription.authorizedOperations())) {
                        mapAcls(consumerGroupDescription, acls);
                    }

                    KafkaConsumerGroupConfigurationEvent kafkaConsumerGroupConfigurationEvent =
                            KafkaConsumerGroupConfigurationEvent.builder()
                                    .kafkaConsumerGroupEvent(mapConsumerGroupEvent(consumerGroupDescription))
                                    .members(mapMembers(consumerGroupDescription))
                                    .partitionAssignor(mapPartitionAssignor(consumerGroupDescription))
                                    .coordinator(mapCoordinator(consumerGroupDescription))
                                    .acls(acls)
                                    .build();

                    configurations.add(kafkaConsumerGroupConfigurationEvent);
                });

        return Collections.unmodifiableList(configurations);
    }
}

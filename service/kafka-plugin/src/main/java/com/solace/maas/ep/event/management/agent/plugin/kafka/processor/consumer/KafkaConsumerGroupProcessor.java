package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.consumer;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.consumer.KafkaConsumerGroupEvent;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.common.ConsumerGroupState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaConsumerGroupProcessor extends ResultProcessorImpl<List<KafkaConsumerGroupEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public KafkaConsumerGroupProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public List<KafkaConsumerGroupEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        log.info("Scan request [{}]: Retrieving [{}] details from Kafka messaging service [{}].",
                properties.get(RouteConstants.SCAN_ID),
                properties.get(RouteConstants.SCAN_TYPE),
                messagingServiceId);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups();

        Collection<ConsumerGroupListing> consumerGroupListings = listConsumerGroupsResult.all()
                .get(30, TimeUnit.SECONDS);

        return consumerGroupListings
                .stream()
                .sorted(Comparator.comparing(ConsumerGroupListing::groupId))
                .map(consumerGroupListing -> KafkaConsumerGroupEvent.builder()
                        .groupId(consumerGroupListing.groupId())
                        .simpleConsumerGroup(consumerGroupListing.isSimpleConsumerGroup())
                        .state(consumerGroupListing.state().orElse(ConsumerGroupState.UNKNOWN).name())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}

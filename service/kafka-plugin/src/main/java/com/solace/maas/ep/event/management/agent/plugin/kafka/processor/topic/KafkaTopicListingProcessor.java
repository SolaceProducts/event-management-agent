package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.topic;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.topic.KafkaTopicEvent;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class KafkaTopicListingProcessor extends ResultProcessorImpl<List<KafkaTopicEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public KafkaTopicListingProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    @SuppressWarnings("PMD")
    public List<KafkaTopicEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        ListTopicsResult listTopicsResult = adminClient.listTopics();
        Collection<TopicListing> topicListings = listTopicsResult.listings()
                .get(30, TimeUnit.SECONDS);

        return topicListings
                .stream()
                .map(topicListing -> KafkaTopicEvent.builder()
                        .topicId(topicListing.topicId().toString())
                        .name(topicListing.name())
                        .internal(topicListing.isInternal())
                        .build())
                .sorted(Comparator.comparing(KafkaTopicEvent::getName))
                .collect(Collectors.toUnmodifiableList());
    }
}

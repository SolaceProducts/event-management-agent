package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.topic;

import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.producer.KafkaProducerEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.producer.KafkaProducerStateEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.topic.KafkaTopicConfigurationEvent;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.runtime.agent.plugin.service.MessagingServiceDelegateService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeProducersResult;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ExcludeFromJacocoGeneratedReport
@Component
@SuppressWarnings("PMD")
public class KafkaTopicProducerProcessor extends ResultProcessorImpl<List<KafkaProducerEvent>, List<KafkaTopicConfigurationEvent>> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public KafkaTopicProducerProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public List<KafkaProducerEvent> handleEvent(Map<String, Object> properties, List<KafkaTopicConfigurationEvent> body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        if (!body.isEmpty()) {
            Collection<TopicPartition> partitions = body.stream()
                    .flatMap(event -> event.getPartitions().stream()
                            .map(partition -> new TopicPartition(event.getName(), partition.getPartition())))
                    .collect(Collectors.toList());

            DescribeProducersResult describeProducersResult = adminClient.describeProducers(partitions);

            return describeProducersResult.all()
                    .get(30, TimeUnit.SECONDS)
                    .entrySet()
                    .stream()
                    .map(entries -> {
                        List<KafkaProducerStateEvent> producerStateEvents = entries.getValue().activeProducers()
                                .stream()
                                .map(producerState -> KafkaProducerStateEvent.builder()
                                        .producerEpoch(producerState.producerEpoch())
                                        .producerId(producerState.producerId())
                                        .coordinatorEpoch(producerState.coordinatorEpoch())
                                        .currentTransactionStartOffset(producerState.currentTransactionStartOffset())
                                        .lastSequence(producerState.lastSequence())
                                        .lastTimestamp(producerState.lastTimestamp())
                                        .build())
                                .collect(Collectors.toUnmodifiableList());

                        return KafkaProducerEvent.builder()
                                .topic(entries.getKey().topic())
                                .partition(entries.getKey().partition())
                                .producerStates(producerStateEvents)
                                .build();
                    }).collect(Collectors.toUnmodifiableList());
        }

        return List.of();
    }
}

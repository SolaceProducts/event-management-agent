package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.consumer;

import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.general.KafkaAclEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.general.KafkaNodeEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaConsumerGroupConfigurationEvent {
    private KafkaConsumerGroupEvent kafkaConsumerGroupEvent;

    private List<ConsumerEvent> members;

    private String partitionAssignor;

    private KafkaNodeEvent coordinator;

    private List<KafkaAclEvent> acls;
}

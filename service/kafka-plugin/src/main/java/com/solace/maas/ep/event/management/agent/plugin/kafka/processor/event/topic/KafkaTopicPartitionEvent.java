package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.topic;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general.KafkaNodeEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaTopicPartitionEvent {
    private Integer partition;

    private List<KafkaNodeEvent> replicas;

    private List<KafkaNodeEvent> isr;

    private KafkaNodeEvent leader;
}

package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.topic;

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
public class KafkaTopicPartitionEvent {
    private Integer partition;

    private List<KafkaNodeEvent> replicas;

    private List<KafkaNodeEvent> isr;

    private KafkaNodeEvent leader;
}

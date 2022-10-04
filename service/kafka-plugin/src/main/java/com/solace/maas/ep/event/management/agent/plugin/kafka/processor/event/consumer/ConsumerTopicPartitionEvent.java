package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConsumerTopicPartitionEvent {
    private Integer partition;

    private String topic;
}

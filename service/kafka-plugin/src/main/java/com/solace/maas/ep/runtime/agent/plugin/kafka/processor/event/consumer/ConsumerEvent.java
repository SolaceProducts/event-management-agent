package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConsumerEvent {
    private String consumerId;

    private String groupInstanceId;

    private String clientId;

    private String host;

    private List<ConsumerTopicPartitionEvent> partitions;
}

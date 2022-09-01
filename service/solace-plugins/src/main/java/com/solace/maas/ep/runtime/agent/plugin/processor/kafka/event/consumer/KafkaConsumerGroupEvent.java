package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaConsumerGroupEvent {
    private String groupId;

    private boolean simpleConsumerGroup;

    private String state;
}

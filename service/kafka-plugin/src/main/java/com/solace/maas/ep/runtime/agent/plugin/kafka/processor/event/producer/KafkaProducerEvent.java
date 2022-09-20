package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaProducerEvent {
    private String topic;

    private Integer partition;

    private List<KafkaProducerStateEvent> producerStates;
}

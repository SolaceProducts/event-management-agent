package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.OptionalInt;
import java.util.OptionalLong;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaProducerStateEvent {
    private Long producerId;

    private Integer producerEpoch;

    private OptionalInt coordinatorEpoch;

    private OptionalLong currentTransactionStartOffset;

    private Integer lastSequence;

    private Long lastTimestamp;
}

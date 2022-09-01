package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaNodeEvent {
    private Integer id;

    private String rack;

    private String host;

    private Integer port;
}

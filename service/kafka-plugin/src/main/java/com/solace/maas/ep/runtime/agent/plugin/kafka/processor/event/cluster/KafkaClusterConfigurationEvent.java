package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaClusterConfigurationEvent {
    private String id;

    private String host;

    private String rack;

    private Integer port;
}

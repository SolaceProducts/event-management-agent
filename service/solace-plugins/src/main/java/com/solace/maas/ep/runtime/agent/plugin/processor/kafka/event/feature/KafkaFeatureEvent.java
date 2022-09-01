package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.feature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaFeatureEvent {
    private String name;

    private String type;

    private Short maxVersionLevel;

    private Short minVersionLevel;
}

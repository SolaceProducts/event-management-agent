package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.feature;

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

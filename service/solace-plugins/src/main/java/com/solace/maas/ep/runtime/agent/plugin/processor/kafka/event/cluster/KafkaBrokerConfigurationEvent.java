package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.cluster;

import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.general.KafkaConfigurationEntryEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaBrokerConfigurationEvent {
    private String name;

    private List<KafkaConfigurationEntryEvent> configurations;
}

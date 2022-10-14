package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.cluster;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general.KafkaConfigurationEntryEvent;
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

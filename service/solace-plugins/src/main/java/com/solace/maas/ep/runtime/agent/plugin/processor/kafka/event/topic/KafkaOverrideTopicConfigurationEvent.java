package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.topic;

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
public class KafkaOverrideTopicConfigurationEvent {
    private String topicName;

    private List<KafkaConfigurationEntryEvent> configurations;
}

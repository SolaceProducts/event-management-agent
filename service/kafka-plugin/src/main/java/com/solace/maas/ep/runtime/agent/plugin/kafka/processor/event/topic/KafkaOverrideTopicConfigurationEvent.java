package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.topic;

import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.general.KafkaConfigurationEntryEvent;
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

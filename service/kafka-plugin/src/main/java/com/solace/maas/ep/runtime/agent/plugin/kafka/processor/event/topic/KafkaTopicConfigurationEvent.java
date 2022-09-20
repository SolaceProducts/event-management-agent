package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.topic;

import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.general.KafkaAclEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaTopicConfigurationEvent {
    private String topicId;

    private String name;

    private Boolean internal;

    private List<KafkaAclEvent> acls;

    private List<KafkaTopicPartitionEvent> partitions;
}

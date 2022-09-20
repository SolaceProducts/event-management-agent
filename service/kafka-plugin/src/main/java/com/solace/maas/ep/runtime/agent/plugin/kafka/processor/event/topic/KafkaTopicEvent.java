package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaTopicEvent implements Serializable {
    private String name;

    private String topicId;

    private Boolean internal;
}

package com.solace.maas.ep.runtime.agent.plugin.processor.kafka.event.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaAclEvent {
    private String name;

    private Boolean unknown;

    private Integer ordinal;
}

package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general;

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

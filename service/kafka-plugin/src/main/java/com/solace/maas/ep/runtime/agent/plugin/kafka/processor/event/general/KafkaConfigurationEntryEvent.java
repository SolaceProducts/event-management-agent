package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaConfigurationEntryEvent {
    private String name;

    private String value;

    private String type;

    private String documentation;

    private Boolean isDefault;

    private Boolean isReadOnly;

    private Boolean isSensitive;
}

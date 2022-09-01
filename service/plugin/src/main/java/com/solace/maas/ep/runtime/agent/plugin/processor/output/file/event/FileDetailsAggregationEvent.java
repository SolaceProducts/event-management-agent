package com.solace.maas.ep.runtime.agent.plugin.processor.output.file.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileDetailsAggregationEvent {
    private String id;

    private String path;

    private String name;

    private String key;
}

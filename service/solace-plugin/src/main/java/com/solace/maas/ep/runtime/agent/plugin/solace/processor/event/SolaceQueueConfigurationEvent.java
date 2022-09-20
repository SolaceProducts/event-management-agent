package com.solace.maas.ep.runtime.agent.plugin.solace.processor.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolaceQueueConfigurationEvent implements Serializable {
    private String name;
    private Map<String, Object> configuration;
}

package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.rules;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Enum {
    private String name;
    private List<String> values;
}

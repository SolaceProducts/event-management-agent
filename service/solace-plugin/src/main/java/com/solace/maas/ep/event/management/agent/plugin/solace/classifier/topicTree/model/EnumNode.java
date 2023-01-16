package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnumNode extends Node {
    private final int MAX_ENUM_PRINT_LENGTH = 200;
    private Collection<String> enums;
    private String fingerPrint;

    @Override
    public String getTopicLevel(Boolean styleTheTopicAddress) {
        if (styleTheTopicAddress) {
            return "(" + getName() + ":" + String.join("|",getEnums()) + ")";
        } else {
            return "(" + getName() + ")";
        }
    }
}

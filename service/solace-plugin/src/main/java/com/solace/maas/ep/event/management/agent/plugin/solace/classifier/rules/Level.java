package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.rules;

import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.Node;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class Level {
    private int level;
    private NodeType nodeType;
    private String name;
    private String regex;
    private List<String> enums;
    private boolean consumable;
    private boolean partialTopicAddressSetLastLevel;
    private Node nodeReference;
}

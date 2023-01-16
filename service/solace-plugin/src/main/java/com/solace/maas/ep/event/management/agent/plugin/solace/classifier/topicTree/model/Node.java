package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Node {
    private NodeType nodeType;

    @EqualsAndHashCode.Exclude
    private NodeContainer children;
    private String name;
    private boolean consumable;
    private boolean newConsumable;
    private boolean newNode;
    @EqualsAndHashCode.Exclude
    private int count;

    @EqualsAndHashCode.Exclude
    private Node parent;
    private int level;
    @EqualsAndHashCode.Exclude
    private int newChildrenCount;
    private String regex;

    public void incrementCount() {
        count += 1;
    }

    public void incrementNewChildrenCount() {
        newChildrenCount += 1;
    }

    public String getTopicLevel(Boolean styleTheTopicAddress) {
        switch (nodeType) {
            case LITERAL:
                return name;
            case VARIABLE:
                if (styleTheTopicAddress) {
                    return "{" + getName() + ":" + getRegex() + "}";
                } else {
                    return "{" + getName() + "}";
                }
            default:
                return "---unknown---";
        }
    }

    public String getTopicPath(boolean styleTheTopicAddress, String separator) {
        if (parent == null) {
            // root node (not a real level)
            return null;
        }

        if (parent.getTopicPath(styleTheTopicAddress, separator) == null) {
            // top topic level, don't add "/" to beginning
            return getTopicLevel(styleTheTopicAddress);
        }

        // General case
        return parent.getTopicPath(styleTheTopicAddress, separator) + separator + getTopicLevel(styleTheTopicAddress);
    }

    public List<Node> getNewChildren() {

        List<Node> newChildren = new ArrayList<>();
        if (children != null && children.getLiteralNodes() != null) {
            newChildren = children.getLiteralNodes().getNodes().values().stream()
                    .filter(node -> node.isNewNode())
                    .collect(Collectors.toList());
        }

        return newChildren;
    }
}

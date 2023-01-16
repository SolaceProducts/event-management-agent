package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree;

import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.EnumNode;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.Node;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeContainer;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class TopicTree {
    private Node rootNode;
    private final String separatorChar;

    public TopicTree(String separatorChar) {
        rootNode = Node.builder()
                .level(0)
                .name("")
                .nodeType(NodeType.LITERAL)
                .children(NodeContainer.builder().build())
                .build();
        this.separatorChar = separatorChar;
    }

    public void resetTree() {
        rootNode.setChildren(NodeContainer.builder().build());
        rootNode.setCount(0);
        rootNode.setNewChildrenCount(0);
    }

    public void dumpTree(NodeContainer children) {
        if (children.getLiteralNodes() != null) {
            for (Node currentNode: children.getLiteralNodes().getNodes().values()) {
                log.info("{} Lit:{}  Lvl:{}  New:{}  NewCons {}", currentNode.getCount(),
                        currentNode.getName(),
                        currentNode.getLevel(),
                        currentNode.isNewNode(),
                        currentNode.isNewConsumable());
                dumpTree(currentNode.getChildren());
            }
        }

        if (children.getVariableNodes() != null) {
            for (Node variableNode: children.getVariableNodes().getNodes().values()) {
                log.info("{} Var: {{}} Lvl:{}  New:{}  NewCons {}", variableNode.getCount(),
                        variableNode.getName(),
                        variableNode.getLevel(),
                        variableNode.isNewNode(),
                        variableNode.isNewConsumable());
                dumpTree(variableNode.getChildren());
            }
        }

        if (children.getEnumNodes() != null) {
            for (EnumNode currentNode: children.getEnumNodes().getNodes().values()) {
                log.info("{} Enm:{}  Lvl:{}  New:{}  NewCons {}", currentNode.getCount(),
                        currentNode.getFingerPrint(),
                        currentNode.getLevel(),
                        currentNode.isNewNode(),
                        currentNode.isNewConsumable());
                dumpTree(currentNode.getChildren());
            }
        }
    }

    public List<Node> getAllLeafNodes(List<Node> currentList, Node currentNode) {
        List<Node> children = currentNode.getNewChildren();

        // Check if the current node is consumable
        if (currentNode.isConsumable() || currentNode.isNewConsumable()) {
            currentList.add(currentNode);
        }

        // Check the children
        if (children.size() > 0) {
            for (Node childNode: children) {
                currentList = getAllLeafNodes(currentList, childNode);
            }
        }

        return currentList;
    }

    public static String getSeparatorAsRegex(String separatorChar) {
        if (!separatorChar.isEmpty() && separatorChar.equals(".")) {
            return "\\" + separatorChar;
        }
        return separatorChar;
    }


}

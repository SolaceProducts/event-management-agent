package com.solace.maas.ep.event.management.agent.plugin.solace.classifier;

import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.TopicTree;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.EnumNode;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.EnumeratedNodes;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.LiteralNodes;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.Node;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeContainer;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeType;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.VariableNodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class TopicMatcher {

    private final TopicTreeService topicTreeService;
    public TopicMatcher(TopicTreeService topicTreeService) {
        this.topicTreeService = topicTreeService;
    }

    public boolean match(String scanId, String topic, String separator) {
        Node currentNode = topicTreeService.getTopicTreeForMessagingService(scanId).getRootNode();
        List<String> rawTopicLevels = List.of(topic.split(TopicTree.getSeparatorAsRegex(separator)));
        boolean matchFound = true;

        int levelCount = 0;
        levelLoop: for (String rawTopicLevel: rawTopicLevels) {
            levelCount += 1;
            boolean lastLevel = levelCount == rawTopicLevels.size();

            // Check for variable match
            NodeContainer existingLevel = currentNode.getChildren();
            VariableNodes existingVariableNodes = existingLevel.getVariableNodes();
            if (existingVariableNodes != null) {
                for(Node variableNode: existingVariableNodes.getNodes().values()) {
                    String regex = (variableNode.getRegex() != null)?variableNode.getRegex():".*";
                    boolean match = false;
                    if (regex != null && !regex.equals(".*")) {
                        if (rawTopicLevel.matches(regex)) {
                            match = true;
                        }
                    } else {
                        // The regex is null or ".*", just assume its a match
                        match = true;
                    }

                    if (match) {
                        variableNode.incrementCount();

                        // Check for consumable level
                        if (!variableNode.isConsumable() && lastLevel) {
                            matchFound = false;
                            variableNode.setNewConsumable(true);
                        }
                        currentNode = variableNode;

                        continue levelLoop;
                   }
               }
            }

            LiteralNodes existingLiteralNodes = existingLevel.getLiteralNodes();
            if (existingLiteralNodes != null && existingLiteralNodes.getNodes().containsKey(rawTopicLevel)) {
                // literal match
                Node matchedNode = existingLiteralNodes.getNodes().get(rawTopicLevel);
                matchedNode.incrementCount();

                // Check for consumable level
                if (!matchedNode.isConsumable() && lastLevel) {
                    matchFound = false;
                    matchedNode.setNewConsumable(true);
                }
                if (matchedNode.isNewNode()) {
                    currentNode.incrementNewChildrenCount();
                }

                currentNode = matchedNode;
                continue levelLoop;
            }

            EnumeratedNodes existingEnumeratedNodes = existingLevel.getEnumNodes();
            if (existingEnumeratedNodes != null) {
                for (EnumNode enumNode: existingEnumeratedNodes.getNodes().values()) {
                    if (enumNode.getEnums().contains(rawTopicLevel)) {

                        enumNode.incrementCount();
                        // Check for consumable level
                        if (!enumNode.isConsumable() && lastLevel) {
                            matchFound = false;
                            enumNode.setNewConsumable(true);
                        }
                        // enum match
                        currentNode = enumNode;
                        continue levelLoop;
                    }
                }
            }

            matchFound = false;

            // No match
            currentNode = noMatchAddNewLevelToTree(currentNode, rawTopicLevel, lastLevel);
        }
        return matchFound;
    }

    private Node noMatchAddNewLevelToTree(Node currentNode, String rawTopicLevel, boolean lastLevel) {
        NodeContainer existingLevel = currentNode.getChildren();

        if (existingLevel.getLiteralNodes() == null) {
            existingLevel.setLiteralNodes(LiteralNodes.builder()
                    .nodes(new HashMap<>())
                    .build());
        }
        NodeContainer newChildren = NodeContainer.builder().build();
        currentNode.incrementNewChildrenCount();
        Node newLiteralNode = Node.builder()
                .name(rawTopicLevel)
                .children(newChildren)
                .consumable(lastLevel)
                .nodeType(NodeType.LITERAL)
                .count(1)
                .parent(currentNode)
                .level(currentNode.getLevel() + 1)
                .newNode(true)
                .build();
        existingLevel.getLiteralNodes().getNodes().put(rawTopicLevel, newLiteralNode);

        return newLiteralNode;
    }
}

package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.trainers;

import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.TopicTreeService;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.rules.Level;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.rules.TopicAddress;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.EnumNode;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.EnumeratedNodes;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.LiteralNodes;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.Node;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeContainer;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeType;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.VariableNodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.solace.maas.ep.event.management.agent.plugin.solace.classifier.ConsoleColors.BLACK_BOLD;
import static com.solace.maas.ep.event.management.agent.plugin.solace.classifier.ConsoleColors.PURPLE_BACKGROUND;
import static com.solace.maas.ep.event.management.agent.plugin.solace.classifier.ConsoleColors.RESET;

@Component
@Slf4j
public class CustomTrainer {

    private final List<TopicAddress> completeTopicAddressList = new ArrayList<>();
    private final List<TopicAddress> partialTopicAddressList = new ArrayList<>();

    // The enums should be stored as global components and potentially reused
    // across topicAddresses
    private final Map<String, Enum> enumMap = new HashMap<>();

    private final String TOPIC_LEVEL_SEPARATOR_STRING = "/";
    private final TopicTreeService topicTreeService;

    public CustomTrainer(ResourceLoader resourceLoader,
                         TopicTreeService topicTreeService) throws FileNotFoundException {

        this.topicTreeService = topicTreeService;

        File topicDirectory = ResourceUtils.getFile("classpath:topic_addresses");
        File[] messagingServicesDirectories = topicDirectory.listFiles();

        for (File messagingServiceDirectory: messagingServicesDirectories) {
            log.info("Messaging service directory {}", messagingServiceDirectory.getName());
            String messagingServiceId = messagingServiceDirectory.getName();
            topicTreeService.initForScan(messagingServiceId, TOPIC_LEVEL_SEPARATOR_STRING);

            List<String> rawTopicAddressList = new ArrayList<>();

            for (File topicAddressesFile: messagingServiceDirectory.listFiles()) {
                log.info("Topic address name {}", topicAddressesFile.getName());

                if ("topic_addresses_complete".equals(topicAddressesFile.getName())) {
                    try (FileReader fr = new FileReader(topicAddressesFile);
                         BufferedReader br = new BufferedReader(fr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            rawTopicAddressList.add(line);
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            List<TopicAddress> topicAddressList = new ArrayList<>();
            topicAddressList.addAll(rawTopicAddressList.stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(rs -> TopicAddress.createTopicAddress(rs, true, TOPIC_LEVEL_SEPARATOR_STRING))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

            trainWithCustomerTopicAddresses(messagingServiceId, topicAddressList);

            log.info("Greg");
        }


//        Resource completeTopicAddressesResource = resourceLoader.getResource("classpath:topic_addresses/topic_addresses_complete");
//        // Get the list of directories
//
//
//        try (Reader reader = new InputStreamReader(completeTopicAddressesResource.getInputStream(), UTF_8)) {
//            topicAddressesRaw = FileCopyUtils.copyToString(reader);
//        }

//        List<String> rawTopicAddressList = List.of(topicAddressesRaw.split("\\r?\\n"));
//
//        completeTopicAddressList.addAll(rawTopicAddressList.stream()
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .map(rs -> TopicAddress.createTopicAddress(rs, true, TOPIC_LEVEL_SEPARATOR_STRING))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList()));
//
//        Resource partialTopicAddressResource = resourceLoader.getResource("classpath:topic_addresses/topic_addresses_partial");
//        String partialTopicAddressRaw;
//        try (Reader reader = new InputStreamReader(partialTopicAddressResource.getInputStream(), UTF_8)) {
//            partialTopicAddressRaw = FileCopyUtils.copyToString(reader);
//        }
//
//        List<String> partialRawTopicAddressList = List.of(partialTopicAddressRaw.split("\\r?\\n"));
//
//        partialTopicAddressList.addAll(partialRawTopicAddressList.stream()
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .map(rs -> TopicAddress.createTopicAddress(rs, false, TOPIC_LEVEL_SEPARATOR_STRING))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList()));

    }

    public void trainWithCustomerTopicAddresses(String messagingServiceId, List<TopicAddress> topicAddressList) {
        // Full Topic Addresses
        for (TopicAddress ta: topicAddressList) {
            trainWithTopicAddress(topicTreeService.getTopicTreeForMessagingService(messagingServiceId).getRootNode(), ta);
        }

        // Partial Topic Addresses
        for (TopicAddress ta: partialTopicAddressList) {
            trainWithTopicAddress(topicTreeService.getTopicTreeForMessagingService(messagingServiceId).getRootNode(), ta);
        }

    }

    public void addCustomTopicAddresses(List<String> topicAddresses, boolean complete) {

        // Remove all partial topic address that are a substring of the new topic addresses
        if (!complete) {
            ListIterator<TopicAddress> taIter = partialTopicAddressList.listIterator();
            while (taIter.hasNext()) {
                TopicAddress currentTopicAddress = taIter.next();
                String topicAddressString = currentTopicAddress.printTopicAddress(true, TOPIC_LEVEL_SEPARATOR_STRING);
                if (topicAddresses.stream().anyMatch(ta -> ta.contains(topicAddressString))) {
                    taIter.remove();
                }
            }
        }

        List<TopicAddress> newTopicAddresses = topicAddresses.stream()
                .map(r -> TopicAddress.createTopicAddress(r, complete, TOPIC_LEVEL_SEPARATOR_STRING))
                .collect(Collectors.toList());
        if (complete) {
            completeTopicAddressList.addAll(newTopicAddresses);
        } else {
            partialTopicAddressList.addAll(newTopicAddresses);
        }
    }

    public void trainWithTopicAddress(Node node, TopicAddress topicAddress) {
        Node currentNode = node;
        for (Level topicAddressLevel: topicAddress.getLevels()) {
            currentNode = addTopicAddressLevelToTopicTree(currentNode, topicAddressLevel);

            // If this is a partial topic address, keep a reference
            // to the node at the last level (if it's not already
            // marked as consumable by a full topic address or EP topic
            // address) since we want to know if it matches any
            // topic addresses. If it does match a topic address
            // then we'll want to move it to the full topic address list.
            if (topicAddressLevel.isPartialTopicAddressSetLastLevel() && !currentNode.isConsumable()) {
                topicAddressLevel.setNodeReference(currentNode);
            }
        }
    }

    private Node addTopicAddressLevelToTopicTree(Node currentNode, Level topicAddressLevel) {
        NodeContainer children = currentNode.getChildren();

        if (topicAddressLevel.getNodeType() == NodeType.LITERAL) {
            return addLiteralToTopicTree(currentNode, topicAddressLevel, children);
        }

        if (topicAddressLevel.getNodeType() == NodeType.VARIABLE) {
            return addVariableToTopicTree(currentNode, topicAddressLevel, children);
        }

        if (topicAddressLevel.getNodeType() == NodeType.ENUM) {

            return addEnumToTopicTree(currentNode, topicAddressLevel);

        }

        return currentNode;
    }

    private EnumNode addEnumToTopicTree(Node currentNode, Level topicAddressLevel) {
        NodeContainer currentLevel = currentNode.getChildren();
        EnumNode enumNode = null;

        List<String> orderedEnumInstances = topicAddressLevel.getEnums().stream()
                .sorted()
                .collect(Collectors.toList());
        String enumFingerPrint = String.join(",", orderedEnumInstances);


        if (currentLevel.getEnumNodes() == null) {
            EnumeratedNodes newEnumeratedNodes = EnumeratedNodes.builder()
                    .nodes(new HashMap<>())
                    .build();
            currentLevel.setEnumNodes(newEnumeratedNodes);
        }

        Map<String, EnumNode> enumNodeMap = currentLevel.getEnumNodes().getNodes();
        // TODO 1 - Need to be more sophisticated around determining if an enum
        // TODO 1 - already present. The existing enum could be a subset, so it
        // TODO 1 - would be more correct to add to the existing one
        // TODO 2 - Also enums should be a top level concern like a topic tree
        // TODO 2 - One list of enums for the entire space
        if (enumNodeMap.containsKey(enumFingerPrint)) {
            // That enum is already here
            enumNode = enumNodeMap.get(enumFingerPrint);
        } else {
            NodeContainer newChildren = new NodeContainer();
            enumNode = EnumNode.builder()
                    .nodeType(NodeType.ENUM)
                    .consumable(topicAddressLevel.isConsumable())
                    .fingerPrint(enumFingerPrint)
                    .enums(orderedEnumInstances)
                    .children(newChildren)
                    .name(topicAddressLevel.getName())
                    .level(currentNode.getLevel() + 1)
                    .parent(currentNode)
                    .build();
            enumNodeMap.put(enumFingerPrint, enumNode);
        }
        return enumNode;
    }

    private Node addVariableToTopicTree(Node currentNode, Level topicAddressLevel, NodeContainer children) {
        if (children.getVariableNodes() == null) {
            VariableNodes newVariableNodes = VariableNodes.builder()
                    .nodes(new HashMap<>())
                    .build();
            children.setVariableNodes(newVariableNodes);
        }

        String variableName = topicAddressLevel.getName();

        Node variableNode;
        Map<String, Node> nodeMap = children.getVariableNodes().getNodes();

        if (nodeMap.containsKey(variableName)) {
            // Already there
            variableNode = children.getVariableNodes().getNodes().get(variableName);
        } else {
            // Need to add a variable node
            NodeContainer newChild = new NodeContainer();
            variableNode = Node.builder()
                    .nodeType(NodeType.VARIABLE)
                    .children(newChild)
                    .consumable(topicAddressLevel.isConsumable())
                    .level(topicAddressLevel.getLevel())
                    .name(variableName)
                    .regex(topicAddressLevel.getRegex())
                    .parent(currentNode)
                    .build();
            nodeMap.put(topicAddressLevel.getName(), variableNode);
        }
        return variableNode;
    }

    private Node addLiteralToTopicTree(Node currentNode, Level topicAddressLevel, NodeContainer children) {
        if (children.getLiteralNodes() == null) {
            LiteralNodes newLiteralNodes = LiteralNodes.builder()
                    .nodes(new HashMap<>())
                    .build();
            children.setLiteralNodes(newLiteralNodes);
        }

        String literalName = topicAddressLevel.getName();
        // New literal node
        //log.info("New literal node {}", topicAddressLevel.getName());
        Map<String, Node> nodeMap = children.getLiteralNodes().getNodes();
        Node literalNode;
        if (nodeMap.containsKey(literalName)) {
            // Already there
            literalNode = children.getLiteralNodes().getNodes().get(literalName);
        } else {
            NodeContainer newChildren = new NodeContainer();
            literalNode = Node.builder()
                    .name(literalName)
                    .nodeType(NodeType.LITERAL)
                    .children(newChildren)
                    .consumable(topicAddressLevel.isConsumable())
                    .level(topicAddressLevel.getLevel())
                    .parent(currentNode)
                    .build();
            nodeMap.put(literalName, literalNode);
        }
        return literalNode;
    }

    public List<TopicAddress> getPartialTopicAddressList() {
        return partialTopicAddressList;
    }

    public List<TopicAddress> getCompleteTopicAddressList() {
        return completeTopicAddressList;
    }

    public void printTopicAddresses() {
        System.out.println(BLACK_BOLD + PURPLE_BACKGROUND + "             TopicAddresses            " + RESET);
        System.out.println("===== Complete =====");
        getCompleteTopicAddressList().stream()
                .forEach(n -> System.out.println(n.printTopicAddress(true, TOPIC_LEVEL_SEPARATOR_STRING)));
        System.out.println("==== Partial ===");
        getPartialTopicAddressList().stream()
                .forEach(n -> System.out.println(n.printTopicAddress(true, TOPIC_LEVEL_SEPARATOR_STRING)));
    }

    public void movePartialTopicAddressesToCompleteTopicAddresses() {
        ListIterator<TopicAddress> iter = partialTopicAddressList.listIterator();
        while(iter.hasNext()){
            TopicAddress ta = iter.next();
            if(partialTopicAddressHadExactTopicAddressMatch(ta.getLevels())){
                iter.remove();
                completeTopicAddressList.add(ta);
            }
        }
    }

    public boolean partialTopicAddressHadExactTopicAddressMatch(List<Level> levels) {
        boolean partialTopicAddressMatchOnExactTopic =
                levels.stream().anyMatch(l -> l.isPartialTopicAddressSetLastLevel() && l.getNodeReference() != null && l.getNodeReference().isNewConsumable());
        levels.forEach(l -> l.setNodeReference(null));
        return partialTopicAddressMatchOnExactTopic;
    }
}

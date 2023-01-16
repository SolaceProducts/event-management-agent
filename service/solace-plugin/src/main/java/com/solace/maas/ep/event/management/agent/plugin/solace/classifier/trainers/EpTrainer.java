package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.trainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.TopicTree;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@Data
public class EpTrainer {
//
//    private final TopicTree topicTree;
//    private List<EventBO> eventList;
//    private List<TopicValueSetBO> enumList;
//    private final ObjectMapper objectMapper;
//    private final CustomTrainer customTrainer;
//    private final EpClient epClient;
//
//    private final Map<String, EnumData> enums = new HashMap<>();
//
//    enum LevelType {
//        literal, variable, enumuration
//    }
//
//    public EpTrainer(TopicTree topicTree,
//                     ResourceLoader resourceLoader,
//                     ObjectMapper objectMapper,
//                     CustomTrainer customTrainer,
//                     EpClient epClient) throws IOException {
//        this.topicTree = topicTree;
//        this.objectMapper = objectMapper;
//        this.customTrainer = customTrainer;
//        this.epClient = epClient;
//
//        Resource resource = resourceLoader.getResource("classpath:events/rideshareEvents.json");
//        String events;
//        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
//            events = FileCopyUtils.copyToString(reader);
//        }
//
//        Resource resource2 = resourceLoader.getResource("classpath:events/rideshareEnums.json");
//        String enums;
//        try (Reader reader = new InputStreamReader(resource2.getInputStream(), UTF_8)) {
//            enums = FileCopyUtils.copyToString(reader);
//        }
//
//        eventList = objectMapper.readValue(events, new TypeReference<>() {
//        });
//        enumList = objectMapper.readValue(enums, new TypeReference<>() {
//        });
//
//        eventList.stream().forEach(event -> log.info(event.toString()));
//        enumList.stream().forEach(event -> log.info(event.toString()));
//
//        //addEventPortalEventsAsTopicAddresses();
//    }
//    public void addEventPortalEventsAsTopicAddresses() {
//        for (EventBO event: eventList) {
//            addEventToCompleteTopicAddress(event);
//        }
//    }
//
//    public void addEventToCompleteTopicAddress(EventBO event) {
//        TopicAddress topicAddress = new TopicAddress();
//        topicAddress.setFromEventPortal(true);
//        List<TopicNodeBO> topicNodeList = event.getTopicAddress().getTopicNodes();
//        int levelIter = 0;
//        for (TopicNodeBO topicLevel: topicNodeList) {
//            levelIter += 1;
//            boolean lastLevel = levelIter == topicNodeList.size();
//            TopicNodeType topicNodeType = topicLevel.getTopicNodeType();
//            if (topicNodeType.equals(TopicNodeType.literal)) {
//                Level level = Level.builder()
//                        .name(topicLevel.getName())
//                        .nodeType(NodeType.LITERAL)
//                        .consumable(lastLevel)
//                        .build();
//                topicAddress.getLevels().add(level);
//            } else if (topicNodeType.equals(TopicNodeType.variable)) {
//                if (topicLevel.getEnumId() != null) {
//                    // enum
//                    TopicValueSetBO enumInstance = enumList.stream()
//                            .filter(enumur -> enumur.getId().equals(topicLevel.getEnumId()))
//                            .findFirst()
//                            .get();
//                    List<String> orderedEnumInstances = enumInstance.getValues().stream()
//                            .map(entry -> entry.getValue())
//                            .sorted()
//                            .collect(Collectors.toList());
//                    Level level = Level.builder()
//                            .name(topicLevel.getName())
//                            .nodeType(NodeType.ENUM)
//                            .consumable(lastLevel)
//                            .enums(orderedEnumInstances)
//                            .build();
//                    topicAddress.getLevels().add(level);
//                } else {
//                    // variable
//                    Level level = Level.builder()
//                            .name(topicLevel.getName())
//                            .nodeType(NodeType.VARIABLE)
//                            .consumable(lastLevel)
//                            .regex(".*")
//                            .build();
//                    topicAddress.getLevels().add(level);
//                }
//            }
//        }
//        customTrainer.getCompleteTopicAddressList().add(topicAddress);
//    }
//
////    private void addEventToTopicTree(EventBO event) {
////        Node currentLevel = topicTree.getRootNode();
////        List<TopicNodeBO> topicNodeList = event.getTopicAddress().getTopicNodes();
////        for (TopicNodeBO topicLevel: topicNodeList) {
////
////            boolean lastLevel = topicLevel.equals(topicNodeList.get(topicNodeList.size() - 1));
////            currentLevel = addLevelToTopicTree(currentLevel, topicLevel, lastLevel);
////        }
////    }
////
////    private Node addLevelToTopicTree(Node currentLevel, TopicNodeBO level, boolean lastLevel) {
////        if (level.getTopicNodeType().equals(TopicNodeType.literal)) {
////
////            // Handle literal
////            currentLevel = addLiteralLevelToTopicTree(currentLevel, level, lastLevel);
////        }
////
////        if (level.getTopicNodeType().equals(TopicNodeType.variable)) {
////
////            // Check if its an enum or a variable
////            if (level.getEnumId() != null) {
////                currentLevel = addEnumLevelToTopicTree(currentLevel, level, lastLevel);
////            } else {
////                currentLevel = addVariableLevelToTopicTree(currentLevel, level, lastLevel);
////            }
////        }
////        return currentLevel;
////    }
////
////    private Node addVariableLevelToTopicTree(Node currentNode, TopicNodeBO level, boolean lastLevel) {
////        NodeContainer children = currentNode.getChildren();
////
////        if (children.getVariableNodes() == null) {
////            VariableNodes newVariableNodes = VariableNodes.builder()
////                    .nodes(new HashMap<>())
////                    .build();
////            children.setVariableNodes(newVariableNodes);
////        }
////
////        String variableName = level.getName();
////
////        Node variableNode;
////        Map<String, Node> nodeMap = children.getVariableNodes().getNodes();
////
////        if (nodeMap.containsKey(variableName)) {
////            // Already there
////            variableNode = children.getLiteralNodes().getNodes().get(variableName);
////        } else {
////            // Need to add a variable node
////            NodeContainer newChild = new NodeContainer();
////            variableNode = Node.builder()
////                    .nodeType(NodeType.VARIABLE)
////                    .children(newChild)
////                    .consumable(level.isConsumable())
////                    .level(currentNode.getLevel() + 1)
////                    .name(variableName)
////                    .regex(".*")
////                    .parent(currentNode)
////                    .build();
////            nodeMap.put(variableName, variableNode);
////        }
////        return variableNode;
////
////        NodeContainer currentLevel = currentNode.getChildren();
////        Node variableNode = null;
////        // Variable handling
////        if (currentLevel.getVariableNode() != null) {
////            // already a variable, follow it to the children
////            variableNode = currentLevel.getVariableNode();
////        } else {
////            // Need to add a variable node
////            NodeContainer newChild = new NodeContainer();
////            variableNode = Node.builder()
////                    .nodeType(NodeType.VARIABLE)
////                    .children(newChild)
////                    .consumable(level.isConsumable())
////                    .level(currentNode.getLevel() + 1)
////                    .name(level.getName())
////                    .parent(currentNode)
////                    .build();
////            currentLevel.setVariableNode(variableNode);
////        }
////        return variableNode;
////    }
//
////    private Node addEnumLevelToTopicTree(Node currentNode, TopicNodeBO level, boolean lastLevel) {
////        // Enum handling
////        //log.info("Got enum {}", level.getEnumId());
////
////        NodeContainer currentLevel = currentNode.getChildren();
////        EnumNode enumNode = null;
////
////        TopicValueSetBO enumInstance = enumList.stream()
////                .filter(enumur -> enumur.getId().equals(level.getEnumId()))
////                .findFirst()
////                .get();
////        List<String> orderedEnumInstances = enumInstance.getValues().stream()
////                .map(entry -> entry.getValue())
////                .sorted()
////                .collect(Collectors.toList());
////        String enumFingerPrint = String.join(",", orderedEnumInstances);
////
////
////        if (currentLevel.getEnumNodes() == null) {
////            EnumeratedNodes newEnumeratedNodes = EnumeratedNodes.builder()
////                    .nodes(new HashMap<>())
////                    .build();
////            currentLevel.setEnumNodes(newEnumeratedNodes);
////        }
////
////        Map<String, EnumNode> enumNodeMap = currentLevel.getEnumNodes().getNodes();
////        if (enumNodeMap.containsKey(enumFingerPrint)) {
////            // That enum is already here
////            enumNode = enumNodeMap.get(enumFingerPrint);
////        } else {
////            NodeContainer newChildren = new NodeContainer();
////            enumNode = EnumNode.builder()
////                    .nodeType(NodeType.ENUM)
////                    .consumable(level.isConsumable())
////                    .fingerPrint(enumFingerPrint)
////                    .enums(orderedEnumInstances)
////                    .children(newChildren)
////                    .name(level.getName())
////                    .level(currentNode.getLevel() + 1)
////                    .parent(currentNode)
////                    .build();
////            enumNodeMap.put(enumFingerPrint, enumNode);
////        }
////        return enumNode;
////    }
////
////    private Node addLiteralLevelToTopicTree(Node currentNode, TopicNodeBO level, boolean lastLevel) {
////        String literalName = level.getName();
////        NodeContainer children = currentNode.getChildren();
////        Node literalNode;
////
////        if (children.getLiteralNodes() == null) {
////            LiteralNodes newLiteralNodes = LiteralNodes.builder()
////                    .nodes(new HashMap<>())
////                    .build();
////            children.setLiteralNodes(newLiteralNodes);
////        }
////
////        Map<String, Node> nodeMap = children.getLiteralNodes().getNodes();
////        if (nodeMap.containsKey(literalName)) {
////            // Already there
////            literalNode = children.getLiteralNodes().getNodes().get(literalName);
////        } else {
////            NodeContainer newChildren = new NodeContainer();
////            literalNode = Node.builder()
////                    .name(literalName)
////                    .nodeType(NodeType.LITERAL)
////                    .children(newChildren)
////                    .consumable(level.isConsumable())
////                    .level(currentNode.getLevel() + 1)
////                    .parent(currentNode)
////                    .build();
////            nodeMap.put(literalName, literalNode);
////        }
////        return literalNode;
////    }
//
//    public void train() {
//        try {
//            List<Map<String, Object>> enumVersions = epClient.getEnumVersionsForOrg();
//            learnEpEnums(enumVersions);
//            log.info("enumVersions {}", enumVersions);
//
//            List<Map<String, Object>> eventVersions = epClient.getEventVersionsForOrg();
//            learnEpEvents(eventVersions);
//            log.info("enums {}", eventVersions);
//
//            Map<String, Object> eventMeshes = epClient.getEventMeshes();
//            log.info("eventMeshes {}", eventMeshes);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public void learnEpEnums(List<Map<String, Object>> enumList) {
//
//        // TODO - Get Enum name from parent enum
//        enumList.stream().forEach(myEnum -> {
//            List<Map<String, Object>> enumValueMap = (List<Map<String, Object>>) myEnum.get("values");
//            List<String> enumValues = enumValueMap.stream()
//                    .map(en -> (String) en.get("value"))
//                    .collect(Collectors.toList());
//
//            enums.put(myEnum.get("id").toString(),
//                    EnumData.builder()
//                            .enumValues(enumValues)
//                            .name(myEnum.get("id").toString())
//                            .build());
//            log.info(myEnum.toString());
//        });
//    }
//
//    public void learnEpEvents(List<Map<String, Object>> eventList) {
//        eventList.forEach(myEvent -> {
//            Map<String, Object> deliveryDescriptor = (Map<String, Object>) myEvent.get("deliveryDescriptor");
//            String brokerType = (String) deliveryDescriptor.get("brokerType");
//
//            if (!deliveryDescriptor.containsKey("address")) {
//                log.info("What!!!");
//            }
//
//            Map<String, Object> addressMap = (Map<String, Object>) deliveryDescriptor.get("address");
//
//            if (addressMap != null) {
//
//                String addressType = (String) addressMap.get("addressType");
//                List<Map<String, String>> addressLevels  = (List<Map<String, String>>) addressMap.get("addressLevels");
//
//                if (addressType.equals("topic")) {
//                    addAddress(addressLevels);
//                }
//            }
//            //enums.put(myEnum.get("id").toString(), (List<String>) myEnum.get("values"));
//            log.info(myEvent.toString());
//        });
//    }
//
//    private void addAddress(List<Map<String, String>> addressLevels) {
//
//        TopicAddress topicAddress = new TopicAddress();
//        topicAddress.setFromEventPortal(true);
//        log.info("addressLevels {}", addressLevels);
//
//        int levelIter = 0;
//        for (Map<String, String> topicLevel: addressLevels) {
//            String topicLevelName = topicLevel.get("name");
//            levelIter += 1;
//            boolean lastLevel = levelIter == addressLevels.size();
//            LevelType levelType = getTopicTypeFromAddressLevel(topicLevel);
//
//            if (levelType.equals(LevelType.literal)) {
//                Level level = Level.builder()
//                        .name(topicLevelName)
//                        .nodeType(NodeType.LITERAL)
//                        .consumable(lastLevel)
//                        .build();
//                topicAddress.getLevels().add(level);
//            } else if (levelType.equals(LevelType.enumuration)) {
//                String enumId = topicLevel.getOrDefault("enumVersionId", null);
//                EnumData myEnum = enums.get(enumId);
//                List<String> orderedEnumInstances = myEnum.getEnumValues();
//                //.stream().map(en -> );
//                String name = myEnum.getName();
//                // enum
////                TopicValueSetBO enumInstance = enumList.stream()
////                        .filter(enumur -> enumur.getId().equals(topicLevel.getEnumId()))
////                        .findFirst()
////                        .get();
////                List<String> orderedEnumInstances = enumInstance.getValues().stream()
////                        .map(entry -> entry.getValue())
////                        .sorted()
////                        .collect(Collectors.toList());
//                Level level = Level.builder()
//                        .name(name)
//                        .nodeType(NodeType.ENUM)
//                        .consumable(lastLevel)
//                        .enums(orderedEnumInstances)
//                        .build();
//                topicAddress.getLevels().add(level);
//            } else if (levelType.equals(LevelType.variable)) {
//                // variable
//                Level level = Level.builder()
//                        .name(topicLevelName)
//                        .nodeType(NodeType.VARIABLE)
//                        .consumable(lastLevel)
//                        .regex(".*")
//                        .build();
//                topicAddress.getLevels().add(level);
//            }
//        }
//        customTrainer.getCompleteTopicAddressList().add(topicAddress);
//    }
//
//    private LevelType getTopicTypeFromAddressLevel(Map<String, String> addressLevel) {
//        String addressLevelType = addressLevel.get("addressLevelType");
//        String enumId = addressLevel.getOrDefault("enumVersionId", null);
//        switch (addressLevelType) {
//            case "variable": {
//                if (enumId != null) {
//                    return LevelType.enumuration;
//                }
//                return LevelType.variable;
//            }
//            default: {
//                return LevelType.literal;
//            }
//        }
//    }

}

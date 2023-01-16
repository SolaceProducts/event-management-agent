package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.rules;

import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.TopicTree;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeType.ENUM;
import static com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeType.LITERAL;
import static com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model.NodeType.VARIABLE;


@Data
public class TopicAddress {
    private List<Level> levels = new ArrayList<>();
    private static String IGNORE_TOPIC_PREFIX = "//";
    private boolean fromEventPortal;

    public static TopicAddress createTopicAddress(String rawTopicAddress, boolean completeTopicAddress, String separator) {

        if (!rawTopicAddress.startsWith(IGNORE_TOPIC_PREFIX)) {
            TopicAddress topicAddress = new TopicAddress();
            List<String> levels = List.of(rawTopicAddress.split(TopicTree.getSeparatorAsRegex(separator)));
            int levelCount = 1;
            for (String level : levels) {
                boolean completeTopicAddressLastLevel = completeTopicAddress && level.equals(levels.get(levels.size() - 1));
                boolean partialTopicAddressLastLevel = (!completeTopicAddress) && level.equals(levels.get(levels.size() - 1));
                processLevel(level, topicAddress, levelCount, completeTopicAddressLastLevel, partialTopicAddressLastLevel);
                levelCount += 1;
            }
            return topicAddress;
        }
        return null;
    }

    private static TopicAddress processLevel(String level, TopicAddress topicAddress, int levelCount,
                                             boolean consumable, boolean partialTopicAddressLastLevel) {
        if (level.startsWith("{") && level.endsWith("}")) {
            // This is a variable
            String rawVar = level.substring(1, level.length() - 1);
            String variableComponents[] = rawVar.split(":");
            String variableName = variableComponents[0];
            String variableRegex = "*";
            if (variableComponents.length == 2) {
                variableRegex = variableComponents[1];
            }
            Level newLevel = Level.builder()
                    .nodeType(VARIABLE)
                    .level(levelCount)
                    .name(variableName)
                    .regex(variableRegex)
                    .consumable(consumable)
                    .partialTopicAddressSetLastLevel(partialTopicAddressLastLevel)
                    .build();
            topicAddress.getLevels().add(newLevel);

        } else if (level.startsWith("(") && level.endsWith(")")){
            // This is an enum
            String rawEnum = level.substring(1, level.length() - 1);
            String enumComponents[] = rawEnum.split(":");
            String enumName = enumComponents[0];
            String enumValuesRaw = enumComponents[1];
            List<String> enumValues = List.of(enumValuesRaw.split("\\|"));
            Level newLevel = Level.builder()
                    .nodeType(ENUM)
                    .level(levelCount)
                    .name(enumName)
                    .enums(enumValues)
                    .consumable(consumable)
                    .partialTopicAddressSetLastLevel(partialTopicAddressLastLevel)
                    .build();
            topicAddress.getLevels().add(newLevel);

        } else {
            // Assume its a literal
            Level newLevel = Level.builder()
                    .nodeType(LITERAL)
                    .level(levelCount)
                    .name(level)
                    .consumable(consumable)
                    .partialTopicAddressSetLastLevel(partialTopicAddressLastLevel)
                    .build();
            topicAddress.getLevels().add(newLevel);
        }
        return topicAddress;
    }

    public String printTopicAddress(boolean completeTopicAdddress, String separator) {
        StringBuilder sb = new StringBuilder();
        this.getLevels().stream().forEach(l -> {
            if (sb.length() > 0) {
                sb.append(separator);
            }

            switch (l.getNodeType()) {
                case LITERAL:
                    sb.append(l.getName());
                    break;
                case VARIABLE:
                    sb.append("{" + l.getName());
                    if (completeTopicAdddress) {
                        sb.append(":" + l.getRegex());
                    }
                    sb.append("}");
                    break;
                case ENUM:
                    sb.append("(" + l.getName());
                    if (completeTopicAdddress) {
                        sb.append(":" + String.join("|", l.getEnums().stream().sorted().collect(Collectors.toList())));
                    }
                    sb.append(")");
                    break;
            };
        });
        return sb.toString();
    }
}

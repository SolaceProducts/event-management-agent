package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiteralNodes {
    Map<String, Node> nodes = new HashMap<>();
}

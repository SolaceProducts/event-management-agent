package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeContainer {
    LiteralNodes literalNodes;
    EnumeratedNodes enumNodes;
    VariableNodes variableNodes;
}
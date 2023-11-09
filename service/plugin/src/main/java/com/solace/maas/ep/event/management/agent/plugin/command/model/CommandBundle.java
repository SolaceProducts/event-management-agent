package com.solace.maas.ep.event.management.agent.plugin.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandBundle {
    private String executionType; // (serial / parallel)
    private Boolean exitOnFailure;
    private List<Command> commandList;
}

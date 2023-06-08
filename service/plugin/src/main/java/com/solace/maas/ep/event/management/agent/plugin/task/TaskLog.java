package com.solace.maas.ep.event.management.agent.plugin.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskLog {
    private String action;
    private Object info;
}

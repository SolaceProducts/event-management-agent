package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResult {
    private boolean success;
    private TaskState state;
    private Object data;
    private TaskLog log;

}

package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task;

import lombok.Builder;
import lombok.Data;

@Data
public class TaskConfig<T> {
    private TaskState taskState;
    private T configObject;
}

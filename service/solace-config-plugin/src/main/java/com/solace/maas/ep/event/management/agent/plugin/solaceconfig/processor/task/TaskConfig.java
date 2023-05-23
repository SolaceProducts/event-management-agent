package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class TaskConfig<T> {
    @NonNull
    private TaskState taskState;
    @NonNull
    private T configObject;
}

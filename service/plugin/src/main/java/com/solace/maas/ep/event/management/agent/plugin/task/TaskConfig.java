package com.solace.maas.ep.event.management.agent.plugin.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskConfig<T> {
    @NonNull
    private TaskState state;
    @NonNull
    private T configObject;
    @NonNull
    private String objectType;

}

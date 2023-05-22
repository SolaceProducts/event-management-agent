package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskState;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class SEMPv2MsgVpnTaskConfig<T> extends TaskConfig<T> {
    @NonNull
    private String objectType;

    @Builder
    public SEMPv2MsgVpnTaskConfig(TaskState state, T configObject, String objectType) {
        super(state, configObject);
        this.objectType = objectType;
    }
}

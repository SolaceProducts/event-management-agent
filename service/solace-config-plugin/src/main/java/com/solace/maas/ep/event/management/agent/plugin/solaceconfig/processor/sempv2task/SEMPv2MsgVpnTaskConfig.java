package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.sempv2task;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskConfig;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class SEMPv2MsgVpnTaskConfig<T> extends TaskConfig<T> {
    private String objectType;
}

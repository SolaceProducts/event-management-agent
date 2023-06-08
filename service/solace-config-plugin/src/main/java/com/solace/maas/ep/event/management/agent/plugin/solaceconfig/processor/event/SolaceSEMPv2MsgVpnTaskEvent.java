package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.event;

import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolaceSEMPv2MsgVpnTaskEvent implements Serializable  {
    private String objectName;
    private TaskConfig data;
}

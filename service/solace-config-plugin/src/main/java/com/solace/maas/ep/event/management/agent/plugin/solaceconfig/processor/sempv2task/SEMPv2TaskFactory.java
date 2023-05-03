package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.sempv2task;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.ITask;
import org.springframework.stereotype.Component;

@Component
public class SEMPv2TaskFactory {
    public SEMPv2TaskFactory() {

    }

    public ITask<?> getTask(SEMPv2MsgVpnTaskConfig<?> config, SolaceSempApiClient client){

        return new AclProfileClientConnectExceptionTask(config, client);
    }
}

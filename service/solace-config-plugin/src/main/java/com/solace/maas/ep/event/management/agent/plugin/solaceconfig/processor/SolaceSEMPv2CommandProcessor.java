package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.sempv2task.SEMPv2MsgVpnTaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.sempv2task.SEMPv2TaskFactory;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.ITask;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SolaceSEMPv2CommandProcessor  extends ResultProcessorImpl<TaskResult, SEMPv2MsgVpnTaskConfig<?>> {

    public SEMPv2TaskFactory taskFactory;
    private final MessagingServiceDelegateService messagingServiceDelegateService;
    @Autowired
    public SolaceSEMPv2CommandProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Autowired
    public void setSEMPv2TaskFactory(SEMPv2TaskFactory taskFactory){
        this.taskFactory = taskFactory;
    }

    @Override
    @SuppressWarnings("PMD")
    public TaskResult handleEvent(Map<String, Object> properties, SEMPv2MsgVpnTaskConfig<?> body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        SolaceSempApiClient client = this.messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);
        ITask<?> t = this.taskFactory.getTask(body, client);

        return t.execute();
    }
}

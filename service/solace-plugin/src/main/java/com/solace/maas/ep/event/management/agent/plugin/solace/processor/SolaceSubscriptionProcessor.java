package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.solace.processor.event.SolaceQueueNameEvent;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SolaceSubscriptionProcessor extends ResultProcessorImpl<List<Map<String, Object>>, List<SolaceQueueNameEvent>> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public SolaceSubscriptionProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public List<Map<String, Object>> handleEvent(Map<String, Object> properties, List<SolaceQueueNameEvent> data) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        SolaceHttpSemp sempClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);
        List<Map<String, Object>> subscriptionsRaw = new ArrayList<>();

        for(SolaceQueueNameEvent queue: data) {
            subscriptionsRaw.addAll(sempClient.getSubscriptionForQueue(queue.getName()));
        }

        return subscriptionsRaw;
    }
}

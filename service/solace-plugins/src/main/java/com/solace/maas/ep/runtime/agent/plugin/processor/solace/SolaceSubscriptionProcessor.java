package com.solace.maas.ep.runtime.agent.plugin.processor.solace;

import com.solace.maas.ep.runtime.agent.plugin.processor.solace.event.SolaceSubscriptionEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.semp.SolaceHttpSemp;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.event.SolaceQueueNameEvent;
import com.solace.maas.ep.runtime.agent.plugin.service.MessagingServiceDelegateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SolaceSubscriptionProcessor extends ResultProcessorImpl<List<SolaceSubscriptionEvent>, List<SolaceQueueNameEvent>> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public SolaceSubscriptionProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public List<SolaceSubscriptionEvent> handleEvent(Map<String, Object> properties, List<SolaceQueueNameEvent> data) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        SolaceHttpSemp sempClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        List<SolaceSubscriptionEvent> solaceSubscriptionEvents = new ArrayList<>();
        for(SolaceQueueNameEvent queue: data) {
            List<Map<String, Object>> subscriptionsRaw = new ArrayList<>();
            subscriptionsRaw.addAll(sempClient.getSubscriptionForQueue(queue.getName()));

            List<String> subscriptions = subscriptionsRaw.stream()
                    .map(subMap -> subMap.get("subscriptionTopic").toString())
                    .collect(Collectors.toUnmodifiableList());

            solaceSubscriptionEvents.add(
                    SolaceSubscriptionEvent.builder()
                            .queueName(queue.getName())
                            .subscriptionList(subscriptions)
                            .build());
        }

        return solaceSubscriptionEvents;
    }
}

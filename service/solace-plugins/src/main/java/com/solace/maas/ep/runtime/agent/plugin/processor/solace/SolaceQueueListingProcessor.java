package com.solace.maas.ep.runtime.agent.plugin.processor.solace;

import com.solace.maas.ep.runtime.agent.plugin.processor.solace.semp.SolaceHttpSemp;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.event.SolaceQueueNameEvent;
import com.solace.maas.ep.runtime.agent.plugin.service.MessagingServiceDelegateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SolaceQueueListingProcessor extends ResultProcessorImpl<List<SolaceQueueNameEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public SolaceQueueListingProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    @SuppressWarnings("PMD")
    public List<SolaceQueueNameEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        SolaceHttpSemp sempClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        Map<String, Map<String, Object>> queueMap = new HashMap<>();
        List<Map<String, Object>> queueResponse = sempClient.getQueueNames();
        for (Map<String, Object> queue : queueResponse) {
            String queueName = (String) queue.get("queueName");
            queueMap.put(queueName, queue);
        }

        return queueMap.keySet().stream()
                .map(qName -> SolaceQueueNameEvent.builder().name(qName).build())
                .sorted(Comparator.comparing(SolaceQueueNameEvent::getName))
                .collect(Collectors.toUnmodifiableList());
    }
}

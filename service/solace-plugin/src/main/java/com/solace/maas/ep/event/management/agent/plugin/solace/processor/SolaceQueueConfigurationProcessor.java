package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SolaceQueueConfigurationProcessor extends ResultProcessorImpl<List<Map<String, Object>>, Object> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public SolaceQueueConfigurationProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    @SuppressWarnings("PMD")
    public List<Map<String, Object>> handleEvent(Map<String, Object> properties, Object body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        log.info("Scan request [{}]: Retrieving [{}] details from Solace messaging service [{}].",
                properties.get(RouteConstants.SCAN_ID),
                properties.get(RouteConstants.SCAN_TYPE),
                messagingServiceId);

        SolaceHttpSemp sempClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        List<Map<String, Object>> queueResponse = sempClient.getQueues();

        return queueResponse;
    }
}

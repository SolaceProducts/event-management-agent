package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event.ConfluentSchemaRegistrySchemaEvent;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ConfluentSchemaRegistrySchemaProcessor extends ResultProcessorImpl<List<ConfluentSchemaRegistrySchemaEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public ConfluentSchemaRegistrySchemaProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    @SuppressWarnings("PMD")
    public List<ConfluentSchemaRegistrySchemaEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        ConfluentSchemaRegistryHttp confluentSchemaRegistryHttp =
                messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        List<ConfluentSchemaRegistrySchemaEvent> confluentSchemaRegistrySchemaEventList = confluentSchemaRegistryHttp.getSchemas();
        return confluentSchemaRegistrySchemaEventList;
    }
}

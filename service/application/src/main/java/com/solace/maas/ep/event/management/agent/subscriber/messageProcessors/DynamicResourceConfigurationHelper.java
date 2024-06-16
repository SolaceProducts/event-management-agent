package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.service.EventBrokerResourceConfigToEventConverter;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Slf4j
public class DynamicResourceConfigurationHelper {

    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    private final EventBrokerResourceConfigToEventConverter eventBrokerResourceConfigToEventConverter;

    public DynamicResourceConfigurationHelper(MessagingServiceDelegateServiceImpl messagingServiceDelegateService,
                                              EventBrokerResourceConfigToEventConverter eventBrokerResourceConfigToEventConverter) {
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.eventBrokerResourceConfigToEventConverter = eventBrokerResourceConfigToEventConverter;
    }

    public void loadSolaceBrokerResourceConfigurations(List<EventBrokerResourceConfiguration> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }
        try {

            List<MessagingServiceEvent> solaceEventBrokerResources = resources.stream()
                    .filter(resource -> resource.getResourceConfigurationType() == ResourceConfigurationType.SOLACE)
                    .map(eventBrokerResourceConfigToEventConverter::mapToMessagingServiceEvent)
                    .toList();
            messagingServiceDelegateService.addMessagingServices(solaceEventBrokerResources)
                    .forEach(messagingServiceEntity ->
                            log.info("Loaded [{}] resource with id: [{}] and name: [{}] from message payload.",
                                    messagingServiceEntity.getType(),
                                    messagingServiceEntity.getId(), messagingServiceEntity.getName()));

        } catch (Exception e) {
            log.error("Error while loading dynamic resource configuration from message payload", e);
            throw new IllegalArgumentException(e);
        }
    }
}


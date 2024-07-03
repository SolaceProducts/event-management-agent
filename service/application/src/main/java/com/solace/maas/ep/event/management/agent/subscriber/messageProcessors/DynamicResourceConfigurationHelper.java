package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.SolaceResourceConfigurationToEventConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Slf4j
public class DynamicResourceConfigurationHelper {

    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    private final SolaceResourceConfigurationToEventConverter solaceResourceConfigurationToEventConverter;

    public DynamicResourceConfigurationHelper(MessagingServiceDelegateServiceImpl messagingServiceDelegateService,
                                              SolaceResourceConfigurationToEventConverter solaceResourceConfigurationToEventConverter) {
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.solaceResourceConfigurationToEventConverter = solaceResourceConfigurationToEventConverter;
    }

    public void loadSolaceBrokerResourceConfigurations(List<EventBrokerResourceConfiguration> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }
        try {

            List<MessagingServiceEvent> solaceEventBrokerResources = resources.stream()
                    .filter(resource -> resource.getResourceConfigurationType() == ResourceConfigurationType.SOLACE)
                    .map(solaceResourceConfigurationToEventConverter::mapToMessagingServiceEvent)
                    .toList();

            messagingServiceDelegateService.upsertMessagingServiceEvents(solaceEventBrokerResources)
                    .forEach(messagingServiceEntity ->
                            log.debug("Loaded [{}] resource with id: [{}] and name: [{}] from message payload.",
                                    messagingServiceEntity.getType(),
                                    messagingServiceEntity.getId(), messagingServiceEntity.getName()));

        } catch (Exception e) {
            log.error("Error while loading dynamic resource configuration from message payload", e);
            throw new IllegalArgumentException(e);
        }
    }
}


package com.solace.maas.ep.runtime.agent.plugin.kafka.processor.topic;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.general.KafkaConfigurationEntryEvent;
import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.topic.KafkaOverrideTopicConfigurationEvent;
import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.event.topic.KafkaTopicEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.runtime.agent.plugin.service.MessagingServiceDelegateService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ExcludeFromJacocoGeneratedReport
@Component
@SuppressWarnings("PMD")
public class KafkaOverrideTopicConfigurationProcessor
        extends ResultProcessorImpl<List<KafkaOverrideTopicConfigurationEvent>, List<KafkaTopicEvent>> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public KafkaOverrideTopicConfigurationProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public List<KafkaOverrideTopicConfigurationEvent> handleEvent(Map<String, Object> properties, List<KafkaTopicEvent> body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        if (!body.isEmpty()) {
            List<ConfigResource> configs = body.stream()
                    .map(event -> new ConfigResource(ConfigResource.Type.TOPIC, event.getName()))
                    .collect(Collectors.toUnmodifiableList());

            if (!configs.isEmpty()) {
                return adminClient.describeConfigs(configs).all()
                        .get(30, TimeUnit.SECONDS)
                        .entrySet()
                        .stream()
                        .map(result -> {
                            List<KafkaConfigurationEntryEvent> configurations = result.getValue().entries()
                                    .stream()
                                    .map(entry -> KafkaConfigurationEntryEvent.builder()
                                            .documentation(entry.documentation())
                                            .name(entry.name())
                                            .value(entry.value())
                                            .type(entry.type().name())
                                            .isDefault(entry.isDefault())
                                            .isReadOnly(entry.isReadOnly())
                                            .isSensitive(entry.isSensitive())
                                            .build())
                                    .collect(Collectors.toUnmodifiableList());

                            return KafkaOverrideTopicConfigurationEvent.builder()
                                    .topicName(result.getKey().name())
                                    .configurations(configurations)
                                    .build();
                        }).filter(entry -> !entry.getConfigurations().isEmpty())
                        .collect(Collectors.toUnmodifiableList());
            } else {
                return List.of();
            }
        } else {
            return List.of();
        }
    }
}

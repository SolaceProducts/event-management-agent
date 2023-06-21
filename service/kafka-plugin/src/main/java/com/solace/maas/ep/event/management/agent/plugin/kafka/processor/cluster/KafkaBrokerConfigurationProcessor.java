package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.cluster;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.cluster.KafkaBrokerConfigurationEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.cluster.KafkaClusterConfigurationEvent;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general.KafkaConfigurationEntryEvent;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Component
@SuppressWarnings("PMD")
public class KafkaBrokerConfigurationProcessor extends ResultProcessorImpl<List<KafkaBrokerConfigurationEvent>,
        List<KafkaClusterConfigurationEvent>> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;
    private final long timeout;
    private final TimeUnit timeUnit;

    @Autowired
    public KafkaBrokerConfigurationProcessor(MessagingServiceDelegateService messagingServiceDelegateService,
                                             KafkaClientConfig kafkaClientConfig) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        timeout = kafkaClientConfig.getConnections().getTimeout().getValue();
        timeUnit = kafkaClientConfig.getConnections().getTimeout().getUnit();
    }

    @Override
    @SuppressWarnings("PMD")
    public List<KafkaBrokerConfigurationEvent> handleEvent(Map<String, Object> properties,
                                                           List<KafkaClusterConfigurationEvent> body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        List<ConfigResource> brokers = body.stream()
                .map(clusterEvent -> new ConfigResource(ConfigResource.Type.BROKER, clusterEvent.getId()))
                .collect(Collectors.toUnmodifiableList());

        return adminClient.describeConfigs(brokers).all()
                .get(timeout, timeUnit)
                .entrySet()
                .stream()
                .map(result -> {
                    List<KafkaConfigurationEntryEvent> configurations = result.getValue().entries()
                            .stream()
                            .filter(entry -> !entry.isDefault())
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

                    return KafkaBrokerConfigurationEvent.builder()
                            .name(result.getKey().name())
                            .configurations(configurations)
                            .build();
                }).collect(Collectors.toUnmodifiableList());
    }
}

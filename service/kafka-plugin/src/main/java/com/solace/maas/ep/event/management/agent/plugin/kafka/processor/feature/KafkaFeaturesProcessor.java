package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.feature;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.feature.KafkaFeatureEvent;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class KafkaFeaturesProcessor extends ResultProcessorImpl<List<KafkaFeatureEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public KafkaFeaturesProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    @SuppressWarnings("PMD")
    public List<KafkaFeatureEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        List<KafkaFeatureEvent> finalizedFeatures = new ArrayList<>(adminClient.describeFeatures()
                .featureMetadata()
                .get(30, TimeUnit.SECONDS)
                .finalizedFeatures()
                .entrySet()
                .stream()
                .map(entry -> KafkaFeatureEvent.builder()
                        .name(entry.getKey())
                        .type("FINALIZED")
                        .maxVersionLevel(entry.getValue().maxVersionLevel())
                        .minVersionLevel(entry.getValue().minVersionLevel())
                        .build())
                .collect(Collectors.toUnmodifiableList()));

        List<KafkaFeatureEvent> supportedFeatures = adminClient.describeFeatures()
                .featureMetadata()
                .get(30, TimeUnit.SECONDS)
                .supportedFeatures()
                .entrySet()
                .stream()
                .map(entry -> KafkaFeatureEvent.builder()
                        .name(entry.getKey())
                        .type("SUPPORTED")
                        .maxVersionLevel(entry.getValue().maxVersion())
                        .minVersionLevel(entry.getValue().minVersion())
                        .build())
                .collect(Collectors.toUnmodifiableList());

        List<KafkaFeatureEvent> features = new ArrayList<>(finalizedFeatures);
        features.addAll(supportedFeatures);

        return features;
    }
}

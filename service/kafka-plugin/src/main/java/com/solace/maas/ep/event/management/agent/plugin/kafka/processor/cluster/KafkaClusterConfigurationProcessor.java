package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.cluster;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.cluster.KafkaClusterConfigurationEvent;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ExcludeFromJacocoGeneratedReport
@Component
@SuppressWarnings("PMD")
public class KafkaClusterConfigurationProcessor extends ResultProcessorImpl<List<KafkaClusterConfigurationEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    public KafkaClusterConfigurationProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    @SuppressWarnings("PMD")
    public List<KafkaClusterConfigurationEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        AdminClient adminClient = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        return adminClient.describeCluster()
                .nodes()
                .get(30, TimeUnit.SECONDS)
                .stream()
                .map(node -> KafkaClusterConfigurationEvent.builder()
                        .id(node.idString())
                        .host(node.host())
                        .rack(node.rack())
                        .port(node.port())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}

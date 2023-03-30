package com.solace.maas.ep.event.management.agent.plugin.kafka.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaScanType;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Component("kafkaRouteDelegateImpl")
public class KafkaRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public KafkaRouteDelegateImpl() {
        super("KAFKA");
    }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        List<RouteBundle> result = new ArrayList<>();

        KafkaScanType kafkaScanType = null;
        try {
            kafkaScanType = KafkaScanType.valueOf(scanType);
        } catch (Exception e) {
            return List.of();
        }


        switch (kafkaScanType) {
            case KAFKA_TOPIC_LISTING:
                result.add(topicListingRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_TOPIC_CONFIGURATION:
                result.add(topicConfigurationRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_TOPIC_OVERRIDE_CONFIGURATION:
                result.add(overrideTopicConfigurationRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_TOPIC_CONFIGURATION_FULL:
                result.add(completeTopicConfigurationRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_PRODUCERS:
                result.add(topicProducerRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_CONSUMER_GROUPS:
                result.add(consumerGroupsRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_CONSUMER_GROUPS.name()));

                break;
            case KAFKA_CONSUMER_GROUPS_CONFIGURATION:
                result.add(consumerGroupsConfigRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_CONSUMER_GROUPS_CONFIGURATION.name()));

                break;
            case KAFKA_CLUSTER_CONFIGURATION:
                result.add(clusterConfigRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_BROKER_CONFIGURATION:
                result.add(brokerConfigRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_FEATURES:
                result.add(featuresRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case KAFKA_ALL:
                result.add(brokerConfigRouteBundle(destinations, recipients, messagingServiceId));
                result.add(completeTopicConfigurationRouteBundle(destinations, recipients, messagingServiceId));
                result.add(consumerGroupsConfigRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_ALL.name()));

                break;
        }

        return result;
    }

    private RouteBundle featuresRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                            String messagingServiceId) {
        return createRouteBundle(destinations, recipients, KafkaRouteType.KAFKA_FEATURES.label, messagingServiceId,
                KafkaRouteId.KAFKA_FEATURES.label, true);
    }

    private RouteBundle clusterConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                 String messagingServiceId) {
        return createRouteBundle(destinations, recipients, KafkaRouteType.KAFKA_CUSTER_CONFIGURATION.label,
                messagingServiceId, KafkaRouteId.KAFKA_CLUSTER_CONFIGURATION.label, true);
    }

    private RouteBundle brokerConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                String messagingServiceId) {
        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_BROKER_CONFIGURATION.label,
                messagingServiceId, KafkaRouteId.KAFKA_BROKER_CONFIGURATION.label, false);

        return clusterConfigRouteBundle(destinations, List.of(kafkaTopicConfiguration), messagingServiceId);
    }

    private RouteBundle topicListingRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                String messagingServiceId) {
        return createRouteBundle(destinations, recipients, KafkaRouteType.KAFKA_TOPIC_LISTING.label, messagingServiceId,
                KafkaRouteId.KAFKA_TOPIC_LISTING.label, true);
    }

    private RouteBundle topicConfigurationRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                      String messagingServiceId) {
        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_TOPIC_CONFIGURATION.label, messagingServiceId,
                KafkaRouteId.KAFKA_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaTopicConfiguration), messagingServiceId);
    }

    private RouteBundle overrideTopicConfigurationRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                              String messagingServiceId) {
        RouteBundle kafkaOverrideTopicConfiguration = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, messagingServiceId,
                KafkaRouteId.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaOverrideTopicConfiguration), messagingServiceId);
    }

    private RouteBundle completeTopicConfigurationRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                              String messagingServiceId) {
        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_TOPIC_CONFIGURATION.label, messagingServiceId,
                KafkaRouteId.KAFKA_TOPIC_CONFIGURATION.label, false);

        RouteBundle kafkaOverrideTopicConfiguration = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, messagingServiceId,
                KafkaRouteId.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaTopicConfiguration, kafkaOverrideTopicConfiguration),
                messagingServiceId);
    }

    private RouteBundle topicProducerRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                 String messagingServiceId) {
        RouteBundle kafkaTopicProducer = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_PRODUCERS.label, messagingServiceId, KafkaRouteId.KAFKA_PRODUCERS.label, false);

        List<RouteBundle> configRecipients = new ArrayList<>(recipients);
        configRecipients.add(kafkaTopicProducer);

        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, configRecipients,
                KafkaRouteType.KAFKA_TOPIC_CONFIGURATION.label, messagingServiceId,
                KafkaRouteId.KAFKA_TOPIC_CONFIGURATION.label, false);

        RouteBundle kafkaOverrideTopicConfiguration = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, messagingServiceId,
                KafkaRouteId.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaTopicConfiguration, kafkaOverrideTopicConfiguration),
                messagingServiceId);
    }

    private RouteBundle consumerGroupsRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                  String messagingServiceId, String scanType) {
        return createRouteBundle(destinations, recipients, KafkaRouteType.KAFKA_CONSUMER_GROUPS.label, messagingServiceId,
                KafkaRouteId.KAFKA_CONSUMER_GROUPS.label, true);
    }

    private RouteBundle consumerGroupsConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                        String messagingServiceId, String scanType) {
        RouteBundle kafkaConsumerGroupsConfig = createRouteBundle(destinations, recipients,
                KafkaRouteType.KAFKA_CONSUMER_GROUP_CONFIGURATION.label, messagingServiceId,
                KafkaRouteId.KAFKA_CONSUMER_GROUP_CONFIGURATION.label, false);

        return consumerGroupsRouteBundle(destinations, List.of(kafkaConsumerGroupsConfig), messagingServiceId, scanType);
    }
}

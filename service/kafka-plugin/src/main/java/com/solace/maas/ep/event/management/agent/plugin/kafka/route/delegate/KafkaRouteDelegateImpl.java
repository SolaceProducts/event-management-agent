package com.solace.maas.ep.event.management.agent.plugin.kafka.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaScanType;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("kafkaRouteDelegateImpl")
public class KafkaRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public KafkaRouteDelegateImpl() {
        super("KAFKA");
    }

    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        List<RouteBundle> result = new ArrayList<>();

        final KafkaScanType kafkaScanType = KafkaScanType.valueOf(scanType);

        switch (kafkaScanType) {
            case KAFKA_TOPIC_LISTING:
                result.add(topicListingRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_TOPIC_LISTING.name()));

                break;
            case KAFKA_TOPIC_CONFIGURATION:
                result.add(topicConfigurationRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_TOPIC_CONFIGURATION.name()));

                break;
            case KAFKA_TOPIC_OVERRIDE_CONFIGURATION:
                result.add(overrideTopicConfigurationRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_TOPIC_OVERRIDE_CONFIGURATION.name()));

                break;
            case KAFKA_TOPIC_CONFIGURATION_FULL:
                result.add(completeTopicConfigurationRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_TOPIC_CONFIGURATION_FULL.name()));

                break;
            case KAFKA_PRODUCERS:
                result.add(topicProducerRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_PRODUCERS.name()));

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
                result.add(clusterConfigRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_CLUSTER_CONFIGURATION.name()));

                break;
            case KAFKA_BROKER_CONFIGURATION:
                result.add(brokerConfigRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_BROKER_CONFIGURATION.name()));

                break;
            case KAFKA_FEATURES:
                result.add(featuresRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_ALL.name()));

                break;
            case KAFKA_ALL:
                result.add(brokerConfigRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_ALL.name()));
                result.add(completeTopicConfigurationRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_ALL.name()));
                result.add(consumerGroupsConfigRouteBundle(destinations, recipients, messagingServiceId,
                        KafkaScanType.KAFKA_ALL.name()));

                break;
        }

        return result;
    }

    private RouteBundle featuresRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                            String messagingServiceId, String scanType) {
        return createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                KafkaRouteId.KAFKA_FEATURES.label, true);
    }

    private RouteBundle clusterConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                 String messagingServiceId, String scanType) {
        return createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                KafkaRouteId.KAFKA_CLUSTER_CONFIGURATION.label, true);
    }

    private RouteBundle brokerConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                String messagingServiceId, String scanType) {
        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                KafkaRouteId.KAFKA_BROKER_CONFIGURATION.label, false);

        return clusterConfigRouteBundle(destinations, List.of(kafkaTopicConfiguration), messagingServiceId, scanType);
    }

    private RouteBundle topicListingRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                String messagingServiceId, String scanType) {
        return createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                KafkaRouteId.KAFKA_TOPIC_LISTING.label, true);
    }

    private RouteBundle topicConfigurationRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                      String messagingServiceId, String scanType) {
        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, recipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaTopicConfiguration), messagingServiceId, scanType);
    }

    private RouteBundle overrideTopicConfigurationRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                              String messagingServiceId, String scanType) {
        RouteBundle kafkaOverrideTopicConfiguration = createRouteBundle(destinations, recipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaOverrideTopicConfiguration), messagingServiceId, scanType);
    }

    private RouteBundle completeTopicConfigurationRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                              String messagingServiceId, String scanType) {
        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, recipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_TOPIC_CONFIGURATION.label, false);

        RouteBundle kafkaOverrideTopicConfiguration = createRouteBundle(destinations, recipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaTopicConfiguration, kafkaOverrideTopicConfiguration),
                messagingServiceId, scanType);
    }

    private RouteBundle topicProducerRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                 String messagingServiceId, String scanType) {
        RouteBundle kafkaTopicProducer = createRouteBundle(destinations, recipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_PRODUCERS.label, false);

        List<RouteBundle> configRecipients = new ArrayList<>(recipients);
        configRecipients.add(kafkaTopicProducer);

        RouteBundle kafkaTopicConfiguration = createRouteBundle(destinations, configRecipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_TOPIC_CONFIGURATION.label, false);

        RouteBundle kafkaOverrideTopicConfiguration = createRouteBundle(destinations, recipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, false);

        return topicListingRouteBundle(destinations, List.of(kafkaTopicConfiguration, kafkaOverrideTopicConfiguration),
                messagingServiceId, scanType);
    }

    private RouteBundle consumerGroupsRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                  String messagingServiceId, String scanType) {
        return createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                KafkaRouteId.KAFKA_CONSUMER_GROUPS.label, true);
    }

    private RouteBundle consumerGroupsConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                        String messagingServiceId, String scanType) {
        RouteBundle kafkaConsumerGroupsConfig = createRouteBundle(destinations, recipients,
                scanType, messagingServiceId, KafkaRouteId.KAFKA_CONSUMER_GROUP_CONFIGURATION.label, false);

        return consumerGroupsRouteBundle(destinations, List.of(kafkaConsumerGroupsConfig), messagingServiceId, scanType);
    }
}

package com.solace.maas.ep.runtime.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.cluster.KafkaClusterConfigurationProcessor;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaClusterConfigurationDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    @Autowired
    public KafkaClusterConfigurationDataPublisherRouteBuilder(KafkaClusterConfigurationProcessor processor,
                                                              RouteManager routeManager) {
        super(processor, KafkaRouteId.KAFKA_CLUSTER_CONFIGURATION.label, KafkaRouteType.KAFKA_CUSTER_CONFIGURATION.label,
                routeManager);
    }
}

package com.solace.maas.ep.event.management.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.cluster.KafkaClusterConfigurationProcessor;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaClusterConfigurationDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    @Autowired
    public KafkaClusterConfigurationDataPublisherRouteBuilder(KafkaClusterConfigurationProcessor processor,
                                                              RouteManager routeManager, MDCProcessor mdcProcessor,
                                                              EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, KafkaRouteId.KAFKA_CLUSTER_CONFIGURATION.label, KafkaRouteType.KAFKA_CUSTER_CONFIGURATION.label,
                routeManager, mdcProcessor, emptyScanEntityProcessor);
    }
}

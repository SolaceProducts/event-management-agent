package com.solace.maas.ep.runtime.agent.plugin.route.handler.kafka.builder;

import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.cluster.KafkaBrokerConfigurationProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaBrokerConfigurationDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    @Autowired
    public KafkaBrokerConfigurationDataPublisherRouteBuilder(KafkaBrokerConfigurationProcessor processor,
                                                             RouteManager routeManager) {
        super(processor, KafkaRouteId.KAFKA_BROKER_CONFIGURATION.label, KafkaRouteType.KAFKA_BROKER_CONFIGURATION.label,
                routeManager, new GenericListScanIdAggregationStrategy(), 1000);
    }
}

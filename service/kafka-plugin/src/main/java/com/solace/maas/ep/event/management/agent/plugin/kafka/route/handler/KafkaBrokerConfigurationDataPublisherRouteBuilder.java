package com.solace.maas.ep.event.management.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.cluster.KafkaBrokerConfigurationProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaBrokerConfigurationDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    @Autowired
    public KafkaBrokerConfigurationDataPublisherRouteBuilder(KafkaBrokerConfigurationProcessor processor,
                                                             RouteManager routeManager, MDCProcessor mdcProcessor) {
        super(processor, KafkaRouteId.KAFKA_BROKER_CONFIGURATION.label, KafkaRouteType.KAFKA_BROKER_CONFIGURATION.label,
                routeManager, new GenericListScanIdAggregationStrategy(), 1000, mdcProcessor);
    }
}

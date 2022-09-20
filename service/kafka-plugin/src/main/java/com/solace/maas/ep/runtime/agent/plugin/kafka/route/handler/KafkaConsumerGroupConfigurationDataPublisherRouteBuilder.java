package com.solace.maas.ep.runtime.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.consumer.KafkaConsumerGroupConfigurationProcessor;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerGroupConfigurationDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     * @param routeManager The list of Route Destinations the Data Collection events will be streamed to.
     */

    @Autowired
    public KafkaConsumerGroupConfigurationDataPublisherRouteBuilder(KafkaConsumerGroupConfigurationProcessor processor,
                                                                    RouteManager routeManager) {
        super(processor, KafkaRouteId.KAFKA_CONSUMER_GROUP_CONFIGURATION.label,
                KafkaRouteType.KAFKA_CONSUMER_GROUP_CONFIGURATION.label, routeManager,
                new GenericListScanIdAggregationStrategy(), 1000);
    }
}

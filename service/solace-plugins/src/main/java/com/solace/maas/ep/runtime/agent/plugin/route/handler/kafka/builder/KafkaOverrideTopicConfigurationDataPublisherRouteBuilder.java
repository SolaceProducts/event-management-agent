package com.solace.maas.ep.runtime.agent.plugin.route.handler.kafka.builder;

import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.topic.KafkaOverrideTopicConfigurationProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.stereotype.Component;

@Component
public class KafkaOverrideTopicConfigurationDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    public KafkaOverrideTopicConfigurationDataPublisherRouteBuilder(KafkaOverrideTopicConfigurationProcessor processor,
                                                            RouteManager routeManager) {
        super(processor, KafkaRouteId.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label,
                KafkaRouteType.KAFKA_OVERRIDE_TOPIC_CONFIGURATION.label, routeManager,
                new GenericListScanIdAggregationStrategy(), 1000);
    }
}

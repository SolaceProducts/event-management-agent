package com.solace.maas.ep.event.management.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.topic.KafkaTopicConfigurationProcessor;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicConfigurationDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     * @param routeManager The list of Route Destinations the Data Collection events will be streamed to.
     */
    public KafkaTopicConfigurationDataPublisherRouteBuilder(KafkaTopicConfigurationProcessor processor,
                                                            RouteManager routeManager, MDCProcessor mdcProcessor,
                                                            RouteCompleteProcessor routeCompleteProcessor) {
        super(processor, KafkaRouteId.KAFKA_TOPIC_CONFIGURATION.label, KafkaRouteType.KAFKA_TOPIC_CONFIGURATION.label,
                routeManager, new GenericListScanIdAggregationStrategy(), 1000, mdcProcessor, routeCompleteProcessor);
    }
}

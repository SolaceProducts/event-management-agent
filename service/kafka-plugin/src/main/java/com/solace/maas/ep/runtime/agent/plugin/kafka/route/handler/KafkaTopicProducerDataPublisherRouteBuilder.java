package com.solace.maas.ep.runtime.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.topic.KafkaTopicProducerProcessor;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicProducerDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    public KafkaTopicProducerDataPublisherRouteBuilder(KafkaTopicProducerProcessor processor, RouteManager routeManager, MDCProcessor mdcProcessor) {
        super(processor, KafkaRouteId.KAFKA_PRODUCERS.label, KafkaRouteType.KAFKA_PRODUCERS.label, routeManager,
                new GenericListScanIdAggregationStrategy(), 1000, mdcProcessor);
    }
}

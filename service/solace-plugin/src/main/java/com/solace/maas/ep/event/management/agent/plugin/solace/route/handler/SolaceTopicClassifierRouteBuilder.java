package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.SolaceTopicClassifierProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceTopicClassifierRouteBuilder extends DataAggregationRouteBuilder {
    @Autowired
    public SolaceTopicClassifierRouteBuilder(SolaceTopicClassifierProcessor processor, RouteManager routeManager,
                                             MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, SolaceRouteType.SOLACE_TOPIC_CLASSIFIER.label, SolaceRouteType.SOLACE_TOPIC_CLASSIFIER.label,
                routeManager, new GenericListScanIdAggregationStrategy(), 10,
                mdcProcessor, emptyScanEntityProcessor);

    }
}

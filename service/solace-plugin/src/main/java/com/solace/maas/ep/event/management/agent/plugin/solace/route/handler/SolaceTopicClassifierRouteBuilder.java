package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.SolaceTopicClassifierProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteId.SOLACE_TOPIC_CLASSIFIER;

@Component
public class SolaceTopicClassifierRouteBuilder extends DataPublisherRouteBuilder {
    @Autowired
    public SolaceTopicClassifierRouteBuilder(SolaceTopicClassifierProcessor processor, RouteManager routeManager,
                                             MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, SOLACE_TOPIC_CLASSIFIER.label, SOLACE_TOPIC_CLASSIFIER.label,
                routeManager, mdcProcessor, emptyScanEntityProcessor);

    }
}

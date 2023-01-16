package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncDataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.SolaceTopicProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceTopicCollectionRouteBuilder extends AsyncDataPublisherRouteBuilder {
    @Autowired
    public SolaceTopicCollectionRouteBuilder(SolaceTopicProcessor processor,
                                             SolaceTopicCollectionRoutePublisherImpl topicRoutePublisher, RouteManager routeManager,
                                             MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, topicRoutePublisher, SolaceRouteType.SOLACE_TOPIC_COLLECTOR.label,
                SolaceRouteType.SOLACE_TOPIC_COLLECTOR.label, routeManager, mdcProcessor, emptyScanEntityProcessor);

    }
}

package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncDataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.TopicSubscriberProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicSubscriberRouteBuilder extends AsyncDataPublisherRouteBuilder {
    @Autowired
    public TopicSubscriberRouteBuilder(TopicSubscriberProcessor processor,
                                       TopicRoutePublisherImpl topicRoutePublisher, RouteManager routeManager,
                                       MDCProcessor mdcProcessor) {
        super(processor, topicRoutePublisher, "topicSubscriber","topicSubscriber",
                routeManager, mdcProcessor);
    }
}

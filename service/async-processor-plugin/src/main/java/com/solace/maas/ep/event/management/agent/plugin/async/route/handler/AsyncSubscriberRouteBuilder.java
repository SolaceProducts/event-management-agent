package com.solace.maas.ep.event.management.agent.plugin.async.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.async.processor.AsyncSubscriberProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncDataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsyncSubscriberRouteBuilder extends AsyncDataPublisherRouteBuilder {
    @Autowired
    public AsyncSubscriberRouteBuilder(AsyncSubscriberProcessor processor,
                                       TestAsyncRoutePublisherImpl testAsyncRoutePublisher, RouteManager routeManager,
                                       MDCProcessor mdcProcessor) {
        super(processor, testAsyncRoutePublisher, "asyncSubscriber","asyncSubscriber",
                routeManager, mdcProcessor);
    }
}

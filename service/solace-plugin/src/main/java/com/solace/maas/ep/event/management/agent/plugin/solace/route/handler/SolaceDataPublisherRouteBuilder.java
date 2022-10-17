package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.SolaceQueueListingProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public SolaceDataPublisherRouteBuilder(SolaceQueueListingProcessor processor, RouteManager routeManager,
                                           MDCProcessor mdcProcessor, RouteCompleteProcessor routeCompleteProcessor) {
        super(processor, "solaceDataPublisher", "queueListing",
                routeManager, mdcProcessor, routeCompleteProcessor);
    }
}

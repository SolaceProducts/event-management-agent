package com.solace.maas.ep.runtime.agent.plugin.route.handler.solace;

import com.solace.maas.ep.runtime.agent.plugin.processor.solace.SolaceQueueListingProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public SolaceDataPublisherRouteBuilder(SolaceQueueListingProcessor processor, RouteManager routeManager) {
        super(processor, "solaceDataPublisher", "queueListing", routeManager);
    }
}

package com.solace.maas.ep.runtime.agent.plugin.route.handler.solace;

import com.solace.maas.ep.runtime.agent.plugin.processor.solace.SolaceSubscriptionProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.stereotype.Component;

@Component
public class SolaceSubscriptionConfigurationDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     * @param routeManager The list of Route Destinations the Data Collection events will be streamed to.
     */
    public SolaceSubscriptionConfigurationDataPublisherRouteBuilder(SolaceSubscriptionProcessor processor,
                                                                    RouteManager routeManager) {
        super(processor, "solaceSubscriptionConfiguration", "subscriptionConfiguration", routeManager,
                new GenericListScanIdAggregationStrategy(), 10);
    }
}

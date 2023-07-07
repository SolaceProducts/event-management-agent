package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataAggregationRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.SolaceSubscriptionProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteId;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteType;
import org.springframework.stereotype.Component;

@Component
public class SolaceSubscriptionConfigurationDataPublisherRouteBuilder extends DataAggregationRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     * @param routeManager The list of Route Destinations the Data Collection events will be streamed to.
     */
    public SolaceSubscriptionConfigurationDataPublisherRouteBuilder(SolaceSubscriptionProcessor processor,
                                                                    RouteManager routeManager, MDCProcessor mdcProcessor,
                                                                    ScanTypeDescendentsProcessor scanTypeDescendentsProcessor) {
        super(processor, SolaceRouteId.SOLACE_SUBSCRIPTION_CONFIG.label, SolaceRouteType.SOLACE_SUBSCRIPTION_CONFIG.label,
                routeManager, new GenericListScanIdAggregationStrategy(), 10, mdcProcessor, scanTypeDescendentsProcessor);
    }
}

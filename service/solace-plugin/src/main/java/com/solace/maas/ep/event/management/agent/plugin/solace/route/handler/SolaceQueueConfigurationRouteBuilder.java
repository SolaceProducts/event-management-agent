package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.SolaceQueueConfigurationProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteId;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceQueueConfigurationRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public SolaceQueueConfigurationRouteBuilder(SolaceQueueConfigurationProcessor processor, RouteManager routeManager,
                                                MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, SolaceRouteId.SOLACE_QUEUE_CONFIG.label, SolaceRouteType.SOLACE_QUEUE_CONFIG.label,
                routeManager, mdcProcessor, emptyScanEntityProcessor);
    }
}

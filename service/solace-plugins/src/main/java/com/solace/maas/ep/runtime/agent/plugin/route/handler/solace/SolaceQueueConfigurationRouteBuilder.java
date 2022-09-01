package com.solace.maas.ep.runtime.agent.plugin.route.handler.solace;

import com.solace.maas.ep.runtime.agent.plugin.processor.solace.SolaceQueueConfigurationProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceQueueConfigurationRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public SolaceQueueConfigurationRouteBuilder(SolaceQueueConfigurationProcessor processor, RouteManager routeManager) {
        super(processor, "solaceQueueConfiguration", "queueConfiguration", routeManager);
    }
}

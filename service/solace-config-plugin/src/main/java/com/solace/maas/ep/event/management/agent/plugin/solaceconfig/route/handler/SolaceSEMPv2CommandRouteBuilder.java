package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.SolaceSEMPv2CommandProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteId;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolaceSEMPv2CommandRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public SolaceSEMPv2CommandRouteBuilder(SolaceSEMPv2CommandProcessor processor, RouteManager routeManager,
                                           MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, SolaceSEMPv2RouteId.SOLACE_SEMPv2_COMMAND.label, SolaceSEMPv2RouteType.SOLACE_SEMPv2_COMMAND.label,
                routeManager, mdcProcessor, emptyScanEntityProcessor);
    }
}

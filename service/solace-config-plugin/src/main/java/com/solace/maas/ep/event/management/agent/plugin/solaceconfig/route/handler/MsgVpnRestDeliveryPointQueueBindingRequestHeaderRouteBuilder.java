package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.ConfigExecutorRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.RestDeliveryPointQueueBindingTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteId;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgVpnRestDeliveryPointQueueBindingRequestHeaderRouteBuilder extends ConfigExecutorRouteBuilder {
    /**
     * @param processor The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public MsgVpnRestDeliveryPointQueueBindingRequestHeaderRouteBuilder(RestDeliveryPointQueueBindingTaskProcessor processor, RouteManager routeManager,
                                                                        MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, SolaceSEMPv2RouteId.MsgVpnRestDeliveryPointQueueBindingRequestHeader.label, SolaceSEMPv2RouteType.MsgVpnRestDeliveryPointQueueBindingRequestHeader.label,
                routeManager, mdcProcessor);
    }
}

package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AbstractRouteBuilder;
import com.solace.maas.ep.event.management.agent.processor.RouteFailedProcessorImpl;
import org.springframework.stereotype.Component;

@Component
public class ScanStatusFailedPersistenceRouteBuilder extends AbstractRouteBuilder {
    private final RouteFailedProcessorImpl routeFailedProcessor;

    public ScanStatusFailedPersistenceRouteBuilder(RouteFailedProcessorImpl routeFailedProcessor,
                                                   ScanTypeDescendentsProcessor scanTypeDescendentsProcessor) {
        super(scanTypeDescendentsProcessor);
        this.routeFailedProcessor = routeFailedProcessor;
    }

    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:processScanStatusAsFailed")
                .process(routeFailedProcessor);
    }
}

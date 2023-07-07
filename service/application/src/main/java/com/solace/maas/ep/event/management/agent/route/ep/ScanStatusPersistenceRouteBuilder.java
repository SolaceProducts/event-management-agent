package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AbstractRouteBuilder;
import com.solace.maas.ep.event.management.agent.processor.RouteCompleteProcessorImpl;
import org.springframework.stereotype.Component;

@Component
public class ScanStatusPersistenceRouteBuilder extends AbstractRouteBuilder {
    private final RouteCompleteProcessorImpl routeCompleteProcessor;

    public ScanStatusPersistenceRouteBuilder(RouteCompleteProcessorImpl routeCompleteProcessor,
                                             ScanTypeDescendentsProcessor scanTypeDescendentsProcessor) {
        super(scanTypeDescendentsProcessor);
        this.routeCompleteProcessor = routeCompleteProcessor;
    }

    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:processScanStatusAsComplete")
                .process(routeCompleteProcessor)
                .to("direct:perRouteScanStatusPublisher?block=false&failIfNoConsumers=false");
    }
}

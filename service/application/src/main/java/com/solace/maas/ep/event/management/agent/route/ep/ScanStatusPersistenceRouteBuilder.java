package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.processor.RouteCompleteProcessorImpl;
import org.springframework.stereotype.Component;

@Component
public class ScanStatusPersistenceRouteBuilder extends AbstractRouteBuilder {
    private final RouteCompleteProcessorImpl routeCompleteProcessor;

    public ScanStatusPersistenceRouteBuilder(RouteCompleteProcessorImpl routeCompleteProcessor) {
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

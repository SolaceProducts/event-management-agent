package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.processor.ScanDataProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataPublisherRouteBuilder extends RouteBuilder {
    private final ScanDataProcessor processor;

    @Autowired
    public ScanDataPublisherRouteBuilder(ScanDataProcessor processor) {
        super();
        this.processor = processor;
    }

    @Override
    public void configure() throws Exception {
        from("seda:eventPortal")
                .routeId("scanDataPublisher")
                .transform(body().append("\n"))
                .process(processor)
                .to("seda:eventPortalEndOfRoute");

        from("direct:importToEP")
                .routeId("importDataPublisher")
                .transform(body().append("\n"))
                .process(processor);

    }
}

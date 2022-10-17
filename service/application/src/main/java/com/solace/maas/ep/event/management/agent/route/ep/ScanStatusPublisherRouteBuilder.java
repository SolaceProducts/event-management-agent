package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.processor.ScanStatusProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanStatusPublisherRouteBuilder extends RouteBuilder {
    private final ScanStatusProcessor scanStatusProcessor;

    @Autowired
    public ScanStatusPublisherRouteBuilder(ScanStatusProcessor scanStatusProcessor) {
        super();
        this.scanStatusProcessor = scanStatusProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("seda:scanStatusPublisher?blockWhenFull=true&size=1000000")
                .routeId("scanStatusPublisher")
                .process(scanStatusProcessor);
    }
}

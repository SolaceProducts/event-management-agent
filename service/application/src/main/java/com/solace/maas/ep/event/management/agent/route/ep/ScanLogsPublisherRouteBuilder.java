package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.processor.ScanLogsProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanLogsPublisherRouteBuilder extends RouteBuilder {
    private final ScanLogsProcessor scanLogsProcessor;

    @Autowired
    public ScanLogsPublisherRouteBuilder(ScanLogsProcessor processor) {
        super();
        scanLogsProcessor = processor;
    }

    @Override
    public void configure() throws Exception {
        from("seda:scanLogsPublisher?blockWhenFull=true&size=1000000")
                .routeId("scanLogsPublisher")
                .throttle(100)
                .process(scanLogsProcessor);
    }
}

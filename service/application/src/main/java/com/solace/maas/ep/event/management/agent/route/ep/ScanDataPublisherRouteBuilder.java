package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AbstractRouteBuilder;
import com.solace.maas.ep.event.management.agent.processor.ScanDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanDataPublisherRouteBuilder extends AbstractRouteBuilder {
    private final ScanDataProcessor processor;

    @Autowired
    public ScanDataPublisherRouteBuilder(ScanDataProcessor processor,
                                         ScanTypeDescendentsProcessor scanTypeDescendentsProcessor) {
        super(scanTypeDescendentsProcessor);
        this.processor = processor;
    }

    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:eventPortal?block=false&failIfNoConsumers=false")
                .routeId("scanDataPublisher")
                .transform(body().append("\n"))
                .throttle(100)
                .process(processor);

        from("direct:importToEP?block=false&failIfNoConsumers=false")
                .routeId("importDataPublisher")
                .transform(body().append("\n"))
                .throttle(100)
                .process(processor);

    }
}

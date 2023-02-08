package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.processor.ScanDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataPublisherRouteBuilder extends AbstractRouteBuilder {
    private final ScanDataProcessor processor;

    @Autowired
    public ScanDataPublisherRouteBuilder(ScanDataProcessor processor) {
        super();
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

package com.solace.maas.ep.event.management.agent.route.scan;

import com.solace.maas.ep.event.management.agent.processor.StartScanProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ScanRouteBuilder extends RouteBuilder {
    private final StartScanProcessor startScanProcessor;

    public ScanRouteBuilder(StartScanProcessor startScanProcessor) {
        this.startScanProcessor = startScanProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("seda://startScan")
                .process(startScanProcessor);
    }
}

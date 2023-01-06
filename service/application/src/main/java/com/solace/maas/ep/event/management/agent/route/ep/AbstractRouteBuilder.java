package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.route.ep.exceptionhandlers.ScanDataExceptionHandler;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionhandlers.ScanStatusExceptionHandler;
import com.solace.maas.ep.event.management.agent.route.ep.exceptions.ScanDataException;
import com.solace.maas.ep.event.management.agent.route.ep.exceptions.ScanStatusException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(ScanStatusException.class)
                .process(new ScanStatusExceptionHandler())
                .to("direct:overallScanStatusPublisher?block=false&failIfNoConsumers=false")
                .continued(true)
                .end();

        onException(ScanDataException.class)
                .process(new ScanDataExceptionHandler())
                .to("direct:overallScanStatusPublisher?block=false&failIfNoConsumers=false")
                .continued(true)
                .end();
    }
}

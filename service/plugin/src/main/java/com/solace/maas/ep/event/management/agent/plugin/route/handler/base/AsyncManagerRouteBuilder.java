package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.plugin.processor.async.AsyncManagerProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AsyncManagerRouteBuilder extends RouteBuilder {
    private final AsyncManagerProcessor asyncManagerProcessor;

    public AsyncManagerRouteBuilder(AsyncManagerProcessor asyncManagerProcessor) {
        this.asyncManagerProcessor = asyncManagerProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("seda:terminateAsyncProcess")
                .process(asyncManagerProcessor);
    }
}

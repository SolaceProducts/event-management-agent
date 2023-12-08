package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.processor.CommandLogsProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandLogsPublisherRouteBuilder extends RouteBuilder {
    private final CommandLogsProcessor commandLogsProcessor;

    @Autowired
    public CommandLogsPublisherRouteBuilder(CommandLogsProcessor processor) {
        super();
        commandLogsProcessor = processor;
    }

    @Override
    public void configure() throws Exception {
        from("seda:commandLogsPublisher?blockWhenFull=true&size=1000000")
                .routeId("commandLogsPublisher")
                .throttle(100)
                .process(commandLogsProcessor);
    }
}

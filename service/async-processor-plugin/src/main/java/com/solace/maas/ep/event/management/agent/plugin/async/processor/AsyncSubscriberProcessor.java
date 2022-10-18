package com.solace.maas.ep.event.management.agent.plugin.async.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class AsyncSubscriberProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("PROCESSING ASYNC: " + exchange.getIn().getBody());
    }
}

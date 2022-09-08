package com.solace.maas.ep.runtime.agent.plugin.localstorage.config.processor;

import com.solace.maas.ep.runtime.agent.plugin.processor.logging.RouteCompleteProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RouteCompleteProcessorImpl implements Processor, RouteCompleteProcessor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("SCAN_COMPLETE", true);
    }
}

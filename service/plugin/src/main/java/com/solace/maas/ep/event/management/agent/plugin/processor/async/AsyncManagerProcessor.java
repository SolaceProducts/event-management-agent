package com.solace.maas.ep.event.management.agent.plugin.processor.async;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.AsyncManager;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AsyncManagerProcessor implements Processor {
    private final AsyncManager asyncManager;

    @Autowired
    public AsyncManagerProcessor(AsyncManager asyncManager) {
        this.asyncManager = asyncManager;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> signal = exchange.getIn().getBody(Map.class);

        asyncManager.stopAsync(signal.get(RouteConstants.SCAN_ID), signal.get(RouteConstants.SCAN_TYPE));
    }
}

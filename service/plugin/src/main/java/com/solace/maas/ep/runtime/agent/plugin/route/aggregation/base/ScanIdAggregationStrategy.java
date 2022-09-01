package com.solace.maas.ep.runtime.agent.plugin.route.aggregation.base;

import org.apache.camel.Exchange;

public interface ScanIdAggregationStrategy {
    void handleExchange(Exchange exchange);

    void handleExchange(Exchange oldExchange, Exchange newExchange);
}

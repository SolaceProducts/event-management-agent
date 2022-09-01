package com.solace.maas.ep.runtime.agent.plugin.route.aggregation;

import com.solace.maas.ep.runtime.agent.plugin.route.aggregation.base.ScanIdAggregationStrategyImpl;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;

public class GenericListScanIdAggregationStrategy extends ScanIdAggregationStrategyImpl {
    @Override
    public void handleExchange(Exchange exchange) {
        List<Object> aggregatedBody = List.of(exchange.getIn().getBody());

        exchange.getIn().setBody(aggregatedBody);
    }

    @Override
    public void handleExchange(Exchange oldExchange, Exchange newExchange) {
        @SuppressWarnings("unchecked")
        List<Object> oldBody = oldExchange.getIn().getBody(List.class);
        Object newElement = newExchange.getIn().getBody();

        List<Object> newBody = new ArrayList<>(oldBody);
        newBody.add(newElement);

        newExchange.getIn().setBody(newBody);
    }
}

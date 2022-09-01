package com.solace.maas.ep.runtime.agent.plugin.route.aggregation.base;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.Objects;

public abstract class ScanIdAggregationStrategyImpl implements AggregationStrategy, ScanIdAggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (Objects.nonNull(oldExchange)) {

            String oldScanId = oldExchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class);
            String newScanId = newExchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class);

            if (oldScanId.equals(newScanId)) {
                handleExchange(oldExchange, newExchange);
            } else {
                handleExchange(oldExchange);
            }

            return newExchange;
        }

        handleExchange(newExchange);

        return newExchange;
    }
}

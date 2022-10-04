package com.solace.maas.ep.event.management.agent.plugin.localstorage.route.aggregation;

import com.solace.maas.ep.event.management.agent.plugin.constants.AggregationConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

@ExcludeFromJacocoGeneratedReport
public class FileParseAggregationStrategyImpl implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        newExchange.getIn().setHeader(RouteConstants.AGGREGATION_ID,
                oldExchange.getIn().getHeader(RouteConstants.AGGREGATION_ID));
        newExchange.getIn().setHeader(AggregationConstants.OBJECT_KEY,
                oldExchange.getIn().getHeader(AggregationConstants.OBJECT_KEY));
        newExchange.getIn().setHeader(AggregationConstants.AGGREGATION_SIZE,
                oldExchange.getIn().getHeader(AggregationConstants.AGGREGATION_SIZE));
        newExchange.getIn().setHeader(AggregationConstants.COMPLETION_COUNT,
                oldExchange.getIn().getHeader(AggregationConstants.COMPLETION_COUNT));

        return newExchange;
    }
}

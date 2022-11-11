package com.solace.maas.ep.event.management.agent.route.ep.aggregation;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

@ExcludeFromJacocoGeneratedReport
public class FileParseAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        newExchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID,
                oldExchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID));
        newExchange.getIn().setHeader(RouteConstants.SCAN_ID,
                oldExchange.getIn().getHeader(RouteConstants.SCAN_ID));
        newExchange.getIn().setHeader(RouteConstants.SCHEDULE_ID,
                oldExchange.getIn().getHeader(RouteConstants.SCHEDULE_ID));
        newExchange.getIn().setHeader(RouteConstants.SCAN_TYPE,
                oldExchange.getIn().getHeader(RouteConstants.SCAN_TYPE));
        newExchange.getIn().setHeader(RouteConstants.SCAN_STATUS,
                oldExchange.getIn().getHeader(RouteConstants.SCAN_STATUS));
        newExchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE,
                oldExchange.getIn().getHeader(RouteConstants.SCAN_STATUS_TYPE));

        return newExchange;
    }
}
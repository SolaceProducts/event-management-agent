package com.solace.maas.ep.event.management.agent.route.ep.aggregation;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.Objects;

@ExcludeFromJacocoGeneratedReport
public class FileZipperAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if(Objects.isNull(newExchange)) {
            return oldExchange;
        } else {
            String scanId = oldExchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class);
            newExchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            newExchange.getIn().setHeader("FILE_LIST_SIZE", oldExchange.getIn().getHeader("FILE_LIST_SIZE"));
        }

        return newExchange;
    }
}
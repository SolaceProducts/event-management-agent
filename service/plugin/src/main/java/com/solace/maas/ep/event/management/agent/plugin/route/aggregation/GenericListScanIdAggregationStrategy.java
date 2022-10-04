package com.solace.maas.ep.event.management.agent.plugin.route.aggregation;

import com.solace.maas.ep.event.management.agent.plugin.route.aggregation.base.ScanIdAggregationStrategyImpl;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import org.apache.camel.Exchange;
import org.slf4j.MDC;

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

        MDC.put(RouteConstants.SCAN_ID, newExchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class));
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID,
                newExchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID, String.class));
        MDC.put(RouteConstants.SCHEDULE_ID,
                newExchange.getIn().getHeader(RouteConstants.SCHEDULE_ID, String.class));
        MDC.put(RouteConstants.SCAN_TYPE,
                newExchange.getIn().getHeader(RouteConstants.SCAN_TYPE, String.class));
    }
}

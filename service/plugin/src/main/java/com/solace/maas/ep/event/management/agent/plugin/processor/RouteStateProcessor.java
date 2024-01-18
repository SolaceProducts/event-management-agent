package com.solace.maas.ep.event.management.agent.plugin.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import org.apache.camel.Exchange;

import java.util.List;

public abstract class RouteStateProcessor {
    public String getScanType(Exchange exchange) throws Exception {
        String scanType;

        if (exchange.getIn().getHeader(RouteConstants.SCAN_TYPE) instanceof List<?>) {
            scanType = (String) exchange.getIn().getBody();
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
        } else {
            scanType = (String) exchange.getIn().getHeader(RouteConstants.SCAN_TYPE);
        }

        return scanType;
    }

}

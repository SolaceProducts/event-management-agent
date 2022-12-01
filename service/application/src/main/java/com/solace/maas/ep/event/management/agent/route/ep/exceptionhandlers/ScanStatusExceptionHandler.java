package com.solace.maas.ep.event.management.agent.route.ep.exceptionhandlers;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

import static org.apache.camel.language.constant.ConstantLanguage.constant;

@Slf4j
public class ScanStatusExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        String scanType = (String) exchange.getIn().getHeader(RouteConstants.SCAN_TYPE);

        log.error("An error has occurred while determining scan status: ", cause);

        // Sending Error message to client
        exchange.getIn().setHeader("SCAN_STATUS_ERROR", constant(true));
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, List.of(scanType));
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.FAILED);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_DESC, cause.getMessage());
    }
}

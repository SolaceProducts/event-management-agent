package com.solace.maas.ep.event.management.agent.plugin.route.exceptionhandlers;

import com.solace.maas.ep.event.management.agent.plugin.constants.ErrorConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import static org.apache.camel.language.constant.ConstantLanguage.constant;


@Slf4j
public class GeneralExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        log.error("Oops! Something went wrong: {}", cause.toString());

        exchange.getIn().setHeader(ErrorConstants.GENERAL_ERROR, constant(true));
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.FAILED);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_DESC, cause.getMessage());
    }
}
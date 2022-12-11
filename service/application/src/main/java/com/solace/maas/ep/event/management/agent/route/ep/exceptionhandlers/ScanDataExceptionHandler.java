package com.solace.maas.ep.event.management.agent.route.ep.exceptionhandlers;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import static org.apache.camel.language.constant.ConstantLanguage.constant;

@Slf4j
public class ScanDataExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        log.error("An error has occurred while streaming scan data to EP: {}", cause.toString());

        exchange.getIn().setHeader("SCAN_DATA_ERROR", constant(true));
    }
}

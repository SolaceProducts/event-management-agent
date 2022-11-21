package com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import static org.apache.camel.language.constant.ConstantLanguage.constant;

@Slf4j
public class ScanDataImportExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        log.error("An error has occurred while importing scan: ", cause);

        // Sending Error message to client
        exchange.getIn().setHeader("MANUAL_IMPORT_ERROR", constant(true));
    }
}

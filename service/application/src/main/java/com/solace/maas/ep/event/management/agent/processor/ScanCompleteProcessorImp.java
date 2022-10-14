package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.ScanCompleteProcessor;
import com.solace.maas.ep.event.management.agent.service.logging.LoggingService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ScanCompleteProcessorImp implements ScanCompleteProcessor, Processor {
    private final LoggingService loggingService;

    public ScanCompleteProcessorImp(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        loggingService.removeLoggingProcessor(scanId);
    }
}

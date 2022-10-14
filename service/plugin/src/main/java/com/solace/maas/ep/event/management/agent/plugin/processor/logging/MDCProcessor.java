package com.solace.maas.ep.event.management.agent.plugin.processor.logging;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MDCProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        MDC.put(RouteConstants.SCAN_ID,
                exchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class));

        MDC.put(RouteConstants.SCAN_TYPE,
                exchange.getIn().getHeader(RouteConstants.SCAN_TYPE, String.class));

        MDC.put(RouteConstants.SCHEDULE_ID,
                exchange.getIn().getHeader(RouteConstants.SCHEDULE_ID, String.class));

        MDC.put(RouteConstants.MESSAGING_SERVICE_ID,
                exchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID, String.class));
    }
}

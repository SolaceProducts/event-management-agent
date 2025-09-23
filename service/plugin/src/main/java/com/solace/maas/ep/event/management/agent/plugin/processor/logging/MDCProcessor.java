package com.solace.maas.ep.event.management.agent.plugin.processor.logging;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.util.MdcUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MDCProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        MDC.put(RouteConstants.SCHEDULE_ID,
                exchange.getIn().getHeader(RouteConstants.SCHEDULE_ID, String.class));

        MDC.put(RouteConstants.SCAN_ID,
                exchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class));

        MDC.put(RouteConstants.TRACE_ID,
                exchange.getIn().getHeader(RouteConstants.TRACE_ID, String.class));

        MDC.put(RouteConstants.X_B_3_TRACE_ID,
                exchange.getIn().getHeader(RouteConstants.TRACE_ID, String.class));

        MDC.put(RouteConstants.ORG_ID,
                exchange.getIn().getHeader(RouteConstants.ORG_ID, String.class));

        MDC.put(RouteConstants.ORIGIN_ORG_ID,
                exchange.getIn().getHeader(RouteConstants.ORIGIN_ORG_ID, String.class));

        MDC.put(RouteConstants.ACTOR_ID,
                exchange.getIn().getHeader(RouteConstants.ACTOR_ID, String.class));

        MDC.put(RouteConstants.SCAN_TYPE,
                exchange.getIn().getHeader(RouteConstants.SCAN_TYPE, String.class));

        MDC.put(RouteConstants.MESSAGING_SERVICE_ID,
                exchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID, String.class));

        MDC.put(RouteConstants.IS_LINKED,
                MdcUtil.isLinked(exchange.getIn().getHeader(RouteConstants.ORG_ID, String.class),
                        exchange.getIn().getHeader(RouteConstants.ORIGIN_ORG_ID, String.class)) ? "true" : "false");
    }
}

package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.service.lifecycle.ScanLifecycleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import static org.apache.camel.util.function.Suppliers.constant;

@Slf4j
@Component
public class RouteCompleteProcessorImpl implements RouteCompleteProcessor,Processor {
    private final ScanLifecycleService scanLifecycleService;

    public RouteCompleteProcessorImpl(ScanLifecycleService scanLifecycleService) {
        this.scanLifecycleService = scanLifecycleService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        String scanType = (String) exchange.getIn().getHeader(RouteConstants.SCAN_TYPE);

        log.info("Route {} completed for scanId {}", scanType, scanId);
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        if (cause != null) {
            log.error("Error has occurred: ", cause);

            // Sending Error message to client
            exchange.getIn().setHeader("SCAN_ERROR", constant(true));
        } else {
            log.info("Route completed successfully");
            if (scanLifecycleService.scanRouteCompleted(scanId)) {
                // Just in case we're chaining more stuff at the end, set the header to
                // show we're done
                exchange.getIn().setHeader("SCAN_COMPLETE", true);
            }
        }
    }
}

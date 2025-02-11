package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.service.ScanStatusService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class RouteCompleteProcessorImpl extends RouteCompleteProcessor {
    private final ScanStatusService scanStatusService;

    public RouteCompleteProcessorImpl(ScanStatusService scanStatusService) {
        super();
        this.scanStatusService = scanStatusService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);
        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        String scanType = getScanType(exchange);

        scanStatusService.save(scanType, scanId, ScanStatus.COMPLETE);
    }
}

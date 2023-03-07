package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportOverAllStatusProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
    }
}

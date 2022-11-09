package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatusType;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportStatusProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        String fileName = (String) exchange.getIn().getHeader("CamelFileName");
        String scanType = StringUtils.substringAfterLast(fileName, '/').replace(".json", "");
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.PER_ROUTE);
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
    }
}

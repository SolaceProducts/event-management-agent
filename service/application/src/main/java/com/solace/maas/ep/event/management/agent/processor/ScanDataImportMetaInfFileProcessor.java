package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportMetaInfFileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileName = (String) exchange.getIn().getHeader("CamelFileName");
        log.trace("reading file: {}", fileName);

        String[] scanDetails = StringUtils.split(fileName, '/');
        String filepath = scanDetails[0];
        String groupId = scanDetails[1];
        String scanId = scanDetails[2];

        exchange.getIn().setHeader("filepath", filepath);
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
    }
}
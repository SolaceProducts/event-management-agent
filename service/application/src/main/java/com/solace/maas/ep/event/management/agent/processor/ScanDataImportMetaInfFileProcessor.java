package com.solace.maas.ep.event.management.agent.processor;

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
        String camelFileName = (String) exchange.getIn().getHeader("CamelFileName");
        log.trace("reading file: {}", camelFileName);

        String filepath = StringUtils.substringBefore(camelFileName, "/");
        String fileName = StringUtils.substringAfterLast(camelFileName, "/");

        exchange.getIn().setHeader("filepath", filepath);
        exchange.getIn().setHeader("fileName", fileName);
    }
}
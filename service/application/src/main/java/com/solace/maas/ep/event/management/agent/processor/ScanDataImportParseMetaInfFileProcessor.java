package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.TRACE_ID;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanDataImportParseMetaInfFileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        MetaInfFileBO metaInfFileBO = exchange.getIn().getBody(MetaInfFileBO.class);
        String traceId = (String) exchange.getProperty(TRACE_ID);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, metaInfFileBO.getScanId());
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, metaInfFileBO.getMessagingServiceId());
        exchange.getIn().setHeader(RouteConstants.EVENT_MANAGEMENT_ID, metaInfFileBO.getEmaId());
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, metaInfFileBO.getScheduleId());

        List<MetaInfFileDetailsBO> filesDetails = metaInfFileBO.getFiles();
        exchange.getIn().setBody(filesDetails);

        log.debug("Scan import request [{}]: Parsing the zip file, event broker: [{}], EMA Id: [{}], files: [{}], traceId: [{}]",
                metaInfFileBO.getScanId(),
                metaInfFileBO.getMessagingServiceId(),
                metaInfFileBO.getEmaId(),
                filesDetails.stream()
                        .map(MetaInfFileDetailsBO::getFileName)
                        .collect(Collectors.joining(", ")),
                traceId);
    }
}
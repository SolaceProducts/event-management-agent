package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportParseMetaInfFileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        MetaInfFileBO metaInfFileBO = exchange.getIn().getBody(MetaInfFileBO.class);

        exchange.getIn().setHeader(RouteConstants.SCAN_ID, metaInfFileBO.getScanId());
        exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, metaInfFileBO.getMessagingServiceId());
        exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, metaInfFileBO.getScheduleId());

        List<MetaInfFileDetailsBO> filesDetails = metaInfFileBO.getFiles();
        exchange.getIn().setBody(filesDetails);
    }
}
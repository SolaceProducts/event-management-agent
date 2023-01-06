package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportOverAllStatusProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        List<MetaInfFileDetailsBO> files = (List<MetaInfFileDetailsBO>) exchange.getIn().getBody();
        List<String> scanTypes = files.stream().map(MetaInfFileDetailsBO::getDataEntityType).collect(Collectors.toUnmodifiableList());

        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, StringUtils.join(scanTypes,","));
    }
}

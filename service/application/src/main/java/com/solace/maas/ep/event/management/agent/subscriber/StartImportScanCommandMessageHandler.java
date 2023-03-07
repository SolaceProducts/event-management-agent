package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanDataImportMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.EVENT_MANAGEMENT_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCAN_TYPE;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCHEDULE_ID;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class StartImportScanCommandMessageHandler extends SolaceMessageHandler<ScanDataImportMessage> {

    private final ProducerTemplate producerTemplate;

    public StartImportScanCommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber,
            ProducerTemplate producerTemplate) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/v1/startImportScan/>", solaceSubscriber);
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void receiveMessage(String destinationName, ScanDataImportMessage message) {
        List<String> destinations = new ArrayList<>();

        log.debug("Received startImportScan command message: {} for messaging service: {}",
                message, message.getMessagingServiceId());

        String scanTypes = StringUtils.join(message.getScanTypes(), ",");

        producerTemplate.send("direct:continueImportFiles", exchange -> {
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, message.getScanId());
            exchange.getIn().setHeader(SCHEDULE_ID, message.getScheduleId());
            exchange.getIn().setHeader(EVENT_MANAGEMENT_ID, message.getEmaId());
            exchange.getIn().setHeader(MESSAGING_SERVICE_ID, message.getMessagingServiceId());
            exchange.getIn().setHeader(SCAN_TYPE, scanTypes);
        });
    }
}

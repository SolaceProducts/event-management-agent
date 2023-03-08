package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanDataImportMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportFilesEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import com.solace.maas.ep.event.management.agent.service.ManualImportFilesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.EVENT_MANAGEMENT_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.IMPORT_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCAN_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCAN_TYPE;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCHEDULE_ID;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class StartImportScanCommandMessageHandler extends SolaceMessageHandler<ScanDataImportMessage> {

    private final ProducerTemplate producerTemplate;
    private final ManualImportFilesService manualImportFilesService;

    public StartImportScanCommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber,
            ProducerTemplate producerTemplate,
            ManualImportFilesService manualImportFilesService) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/v1/startImportScan/>", solaceSubscriber);
        this.producerTemplate = producerTemplate;
        this.manualImportFilesService = manualImportFilesService;
    }

    @Override
    public void receiveMessage(String destinationName, ScanDataImportMessage message) {
        log.debug("Received startImportScan command message: {} for messaging service: {}",
                message, message.getMessagingServiceId());

        String scanId = message.getScanId();

        String scanTypes = String.join(",", message.getScanTypes());
        List<ManualImportFilesEntity> manualImportFilesEntityList = manualImportFilesService.getAllByScanId(scanId);

        if (manualImportFilesEntityList.isEmpty()) {
            throw new RuntimeException(String.format("can't retrieve any manualImportFiles for scanId: {}", scanId));
        }

        ManualImportFilesEntity firstManualImportFilesEntity = manualImportFilesEntityList.get(0);

        List<MetaInfFileDetailsBO> metaInfFileDetailsBOList = manualImportFilesEntityList.stream()
                .map(manualImportFilesEntity -> MetaInfFileDetailsBO.builder()
                        .fileName(manualImportFilesEntity.getFileName())
                        .dataEntityType(manualImportFilesEntity.getDataEntityType())
                        .build())
                .collect(Collectors.toList());

        producerTemplate.send("direct:continueImportFiles", exchange -> {
            exchange.getIn().setHeader(SCAN_ID, scanId);
            exchange.getIn().setHeader(SCHEDULE_ID, firstManualImportFilesEntity.getScheduleId());
            exchange.getIn().setHeader(EVENT_MANAGEMENT_ID, firstManualImportFilesEntity.getEmaId());
            exchange.getIn().setHeader(IMPORT_ID, firstManualImportFilesEntity.getImportId());
            exchange.getIn().setHeader(MESSAGING_SERVICE_ID, message.getMessagingServiceId());
            exchange.getIn().setHeader(SCAN_TYPE, scanTypes);
            exchange.getIn().setBody(metaInfFileDetailsBOList);
        });
    }
}

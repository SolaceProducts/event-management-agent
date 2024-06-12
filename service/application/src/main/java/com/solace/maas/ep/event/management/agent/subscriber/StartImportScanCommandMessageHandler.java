package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanDataImportMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportFilesEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import com.solace.maas.ep.event.management.agent.service.ManualImportDetailsService;
import com.solace.maas.ep.event.management.agent.service.ManualImportFilesService;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.ProducerTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnExpression("${event-portal.gateway.messaging.standalone:false}== false && ${event-portal.managed:false} == false")
public class StartImportScanCommandMessageHandler extends SolaceDirectMessageHandler<ScanDataImportMessage> {

    private final ProducerTemplate producerTemplate;
    private final ManualImportFilesService manualImportFilesService;
    private final ManualImportDetailsService manualImportDetailsService;

    public StartImportScanCommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber,
            ProducerTemplate producerTemplate,
            ManualImportFilesService manualImportFilesService,
            ManualImportDetailsService manualImportDetailsService) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/v1/startImportScan/>", solaceSubscriber);
        this.producerTemplate = producerTemplate;
        this.manualImportFilesService = manualImportFilesService;
        this.manualImportDetailsService = manualImportDetailsService;
    }

    @Override
    public void receiveMessage(String destinationName, ScanDataImportMessage scanDataImportMessage) {
        String scanId = scanDataImportMessage.getScanId();
        String traceId = scanDataImportMessage.getTraceId();
        String messagingServiceId = scanDataImportMessage.getMessagingServiceId();
        String scanTypes = String.join(",", scanDataImportMessage.getScanTypes());

        List<ManualImportFilesEntity> manualImportFilesEntityList = manualImportFilesService.getAllByScanId(scanId);

        log.trace("Received startImportScan command message: {} for event broker: {}, traceId: {}",
                scanDataImportMessage, messagingServiceId, traceId);

        log.info("Scan import request [{}], trace ID [{}]: Handshake with EP is complete.", scanId, traceId);

        if (manualImportFilesEntityList.isEmpty()) {
            throw new RuntimeException(String.format("can't retrieve any manualImportFiles for scanId: %s", scanId));
        }

        ManualImportDetailsEntity manualImportDetailsEntity = manualImportDetailsService.getByScanId(scanId)
                .orElseThrow(() -> new RuntimeException(String.format("Can't retrieve manualImportDetails by scanId: %s", scanId)));

        List<MetaInfFileDetailsBO> metaInfFileDetailsBOList = manualImportFilesEntityList.stream()
                .map(manualImportFilesEntity -> MetaInfFileDetailsBO.builder()
                        .fileName(manualImportFilesEntity.getFileName())
                        .dataEntityType(manualImportFilesEntity.getDataEntityType())
                        .build())
                .collect(Collectors.toList());

        log.info("Scan import request [{}], trace ID [{}]: Total of {} scan types to be imported: [{}].",
                scanId, traceId, scanDataImportMessage.getScanTypes().size(), StringUtils.join(scanDataImportMessage.getScanTypes(), ", "));

        producerTemplate.send("direct:continueImportingFiles", exchange -> {
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.TRACE_ID, traceId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, manualImportDetailsEntity.getScheduleId());
            exchange.getIn().setHeader(RouteConstants.EVENT_MANAGEMENT_ID, manualImportDetailsEntity.getEmaId());
            exchange.getIn().setHeader(RouteConstants.IMPORT_ID, manualImportDetailsEntity.getImportId());
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanTypes);

            exchange.getIn().setBody(metaInfFileDetailsBOList);
        });
    }
}

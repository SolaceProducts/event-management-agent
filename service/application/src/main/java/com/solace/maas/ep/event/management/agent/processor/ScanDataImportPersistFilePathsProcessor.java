package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportFilesEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import com.solace.maas.ep.event.management.agent.service.ManualImportDetailsService;
import com.solace.maas.ep.event.management.agent.service.ManualImportFilesService;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.EVENT_MANAGEMENT_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.IMPORT_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCAN_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCHEDULE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.TRACE_ID;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanDataImportPersistFilePathsProcessor implements Processor {

    private final ManualImportFilesService manualImportFilesService;
    private final ManualImportDetailsService manualImportDetailsService;
    private final IDGenerator idGenerator;

    public ScanDataImportPersistFilePathsProcessor(ManualImportFilesService manualImportFilesService,
                                                   ManualImportDetailsService manualImportDetailsService,
                                                   IDGenerator idGenerator) {
        this.manualImportFilesService = manualImportFilesService;
        this.manualImportDetailsService = manualImportDetailsService;
        this.idGenerator = idGenerator;
    }

    @Override
    @Transactional
    public void process(Exchange exchange) throws Exception {
        List<MetaInfFileDetailsBO> files = (List<MetaInfFileDetailsBO>) exchange.getIn().getBody();

        String traceId = (String) exchange.getProperty(TRACE_ID);

        Map<String, Object> properties = exchange.getIn().getHeaders();

        String scheduleId = (String) properties.get(SCHEDULE_ID);
        String scanId = (String) properties.get(SCAN_ID);
        String emaId = (String) properties.get(EVENT_MANAGEMENT_ID);
        String importId = (String) exchange.getProperty(IMPORT_ID);

        ManualImportDetailsEntity manualImportDetailsEntity = ManualImportDetailsEntity.builder()
                .id(idGenerator.generateRandomUniqueId())
                .scheduleId(scheduleId)
                .scanId(scanId)
                .traceId(traceId)
                .emaId(emaId)
                .importId(importId)
                .build();

        manualImportDetailsService.save(manualImportDetailsEntity);

        log.debug("saved import details: {}", manualImportDetailsEntity);

        List<ManualImportFilesEntity> manualImportFilesEntityList = files.stream()
                .map(file -> ManualImportFilesEntity.builder()
                        .id(idGenerator.generateRandomUniqueId())
                        .fileName(file.getFileName())
                        .dataEntityType(file.getDataEntityType())
                        .scanId(scanId)
                        .build())
                .collect(Collectors.toList());

        manualImportFilesService.saveAll(manualImportFilesEntityList);

        log.debug("saved import files list: {}", manualImportFilesEntityList);
    }
}

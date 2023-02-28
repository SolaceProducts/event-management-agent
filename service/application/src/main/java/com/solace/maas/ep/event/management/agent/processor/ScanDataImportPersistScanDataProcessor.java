package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import com.solace.maas.ep.event.management.agent.service.ScanTypeService;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ScanDataImportPersistScanDataProcessor implements Processor {

    private final MessagingServiceDelegateService messagingServiceDelegateService;
    private final ScanService scanService;
    private final ScanTypeService scanTypeService;
    private final IDGenerator idGenerator;

    public ScanDataImportPersistScanDataProcessor(MessagingServiceDelegateService messagingServiceDelegateService,
                                                  ScanService scanService, ScanTypeService scanTypeService,
                                                  IDGenerator idGenerator) {
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.scanService = scanService;
        this.scanTypeService = scanTypeService;
        this.idGenerator = idGenerator;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        prepareAndSaveScanDetails(exchange);
    }

    private void prepareAndSaveScanDetails(Exchange exchange) {
        String messagingServiceId = (String) exchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID);
        String emaId = (String) exchange.getIn().getHeader(RouteConstants.EVENT_MANAGEMENT_ID);
        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        String scheduleId = (String) exchange.getIn().getHeader(RouteConstants.SCHEDULE_ID);
        String scanType = (String) exchange.getIn().getHeader(RouteConstants.SCAN_TYPE);

        List<?> files = (List<?>) exchange.getIn().getBody();

        MessagingServiceEntity messagingServiceEntity =
                messagingServiceDelegateService.getMessagingServiceById(messagingServiceId);

        ScanEntity scanEntity = ScanEntity.builder()
                .id(scanId)
                .messagingService(messagingServiceEntity)
                .emaId(emaId)
                .build();

        List<DataCollectionFileEntity> fileEntities = prepareFiles(scanEntity, files, scheduleId, scanId);

        scanEntity.setDataCollectionFiles(fileEntities);

        ScanEntity savedScan = scanService.save(scanEntity);

        prepareAndSaveScanTypes(savedScan, scanType);
    }

    private List<DataCollectionFileEntity> prepareFiles(ScanEntity scanEntity, List<?> metaInfFileDetailsBOS,
                                                        String scheduleId, String scanId) {
        List<String> files = metaInfFileDetailsBOS.stream()
                .map(MetaInfFileDetailsBO.class::cast)
                .map(MetaInfFileDetailsBO::getFileName)
                .collect(Collectors.toUnmodifiableList());

        return files.stream()
                .map(fileEntity ->
                        DataCollectionFileEntity.builder()
                                .path("data_collection/" + scheduleId + "/" + scanId + "/" + fileEntity)
                                .purged(false)
                                .scan(scanEntity)
                                .build())
                .collect(Collectors.toUnmodifiableList());
    }

    private void prepareAndSaveScanTypes(ScanEntity savedScan, String scanType) {
        List<String> scanTypes = Arrays.stream(StringUtils.split(scanType, ","))
                .collect(Collectors.toUnmodifiableList());

        List<ScanTypeEntity> scanTypeEntities = scanTypes.stream().map(type ->
                        ScanTypeEntity.builder()
                                .id(idGenerator.generateRandomUniqueId())
                                .name(type)
                                .scan(savedScan)
                                .build())
                .collect(Collectors.toUnmodifiableList());

        List<ScanStatusEntity> importStatuses = scanTypes.stream().map(type ->
                        ScanStatusEntity.builder()
                                .id(idGenerator.generateRandomUniqueId())
                                .status(ScanStatus.IN_PROGRESS.name())
                                .build())
                .collect(Collectors.toUnmodifiableList());

        IntStream.range(0, importStatuses.size()).forEach(i -> {
            importStatuses.get(i).setScanType(scanTypeEntities.get(i));
            scanTypeEntities.get(i).setStatus(importStatuses.get(i));
        });

        scanTypeService.saveAll(scanTypeEntities);
    }
}

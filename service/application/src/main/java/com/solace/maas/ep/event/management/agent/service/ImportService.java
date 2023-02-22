package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.DataCollectionFileEvent;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileDetailsBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.file.GenericFile;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImportService {

    private final ProducerTemplate producerTemplate;
    private final DataCollectionFileService dataCollectionFileService;
    private final ScanService scanService;
    private final EventPortalProperties eventPortalProperties;

    public ImportService(ProducerTemplate producerTemplate, DataCollectionFileService dataCollectionFileService,
                         ScanService scanService, EventPortalProperties eventPortalProperties) {
        this.producerTemplate = producerTemplate;
        this.dataCollectionFileService = dataCollectionFileService;
        this.scanService = scanService;
        this.eventPortalProperties = eventPortalProperties;
    }

    public void importData(ImportRequestBO importRequestBO) throws IOException {
        boolean isEMAStandalone = eventPortalProperties.getGateway().getMessaging().isStandalone();
        InputStream importStream = importRequestBO.getDataFile().getInputStream();
        String importId = UUID.randomUUID().toString();

        if (isEMAStandalone) {
            throw new FileUploadException("Scan data could not be imported in standalone mode.");
        } else {
            initiateImport(importStream, importId);
        }
    }

    private void initiateImport(InputStream files, String importId) {
        RouteEntity route = RouteEntity.builder()
                .id("importScanData")
                .active(true)
                .build();

        producerTemplate.asyncSend("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader("IMPORT_ID", importId);
            exchange.getIn().setBody(files);
        });
    }

    public InputStream zip(ZipRequestBO zipRequestBO) throws FileNotFoundException {
        String scanId = zipRequestBO.getScanId();

        List<DataCollectionFileEntity> files = dataCollectionFileService.findAllByScanId(scanId);

        ScanEntity scanEntity = scanService.findById(scanId)
                .orElseThrow(() -> {
                    String message = String.format("Could not find scan : [%s].", scanId);
                    log.error(message);
                    return new NoSuchElementException(message);
                });

        String messagingServiceId = scanEntity.getMessagingService().getId();

        String scheduleId = StringUtils.substringBetween(files.stream().findFirst()
                .orElseThrow(() -> {
                    String message = "Could not find scan files.";
                    log.error(message);
                    return new FileNotFoundException(message);
                }).getPath(), "/");

        MetaInfFileBO metaInfJson = prepareMetaInfJson(files, messagingServiceId, scheduleId, scanId);

        List<DataCollectionFileEvent> fileEvents = prepareFileEvents(files, scanId);

        initiateZip(messagingServiceId, scheduleId, scanId, metaInfJson, fileEvents);

        Exchange exchange = downloadZipFile(scanId);
        GenericFile<?> downloadedGenericFile = exchange.getIn().getBody(GenericFile.class);
        File downloadedFile = (File) downloadedGenericFile.getFile();

        return new FileInputStream(downloadedFile);
    }

    private MetaInfFileBO prepareMetaInfJson(List<DataCollectionFileEntity> files, String messagingServiceId,
                                             String scheduleId, String scanId) {

        List<MetaInfFileDetailsBO> metaInfFileDetailsBOFiles = new ArrayList<>();
        files.forEach(file -> {
            String filePath = file.getPath();
            String fileName = StringUtils.substringAfterLast(filePath, "/");
            String dataEntityType = fileName.replace(".json", "");

            MetaInfFileDetailsBO metaInfFileDetailsBOFile =
                    MetaInfFileDetailsBO.builder()
                            .fileName(fileName)
                            .dataEntityType(dataEntityType)
                            .build();
            metaInfFileDetailsBOFiles.add(metaInfFileDetailsBOFile);
        });

        return MetaInfFileBO.builder()
                .files(metaInfFileDetailsBOFiles)
                .scheduleId(scheduleId)
                .scanId(scanId)
                .messagingServiceId(messagingServiceId)
                .build();
    }

    private List<DataCollectionFileEvent> prepareFileEvents(List<DataCollectionFileEntity> files, String scanId) {
        List<DataCollectionFileEvent> fileEvents = files.stream()
                .map(file -> {
                    Path path = Paths.get(file.getPath());

                    return DataCollectionFileEvent.builder()
                            .id(file.getId())
                            .name(path.getFileName().toString())
                            .path(path.getParent().toString())
                            .scanId(scanId).purged(false)
                            .build();
                }).collect(Collectors.toList());

        DataCollectionFileEvent metaInfFileEvent = DataCollectionFileEvent.builder()
                .name("META_INF.json")
                .path(fileEvents.stream().findFirst().orElseThrow().getPath())
                .scanId(scanId).purged(false)
                .build();

        fileEvents.add(metaInfFileEvent);
        return fileEvents;
    }

    private void initiateZip(String messagingServiceId, String scheduleId, String scanId, MetaInfFileBO metaInfJson,
                             List<DataCollectionFileEvent> files) {
        RouteEntity route = RouteEntity.builder()
                .id("writeMetaInfAndZipFiles")
                .active(true)
                .build();

        producerTemplate.send("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, scheduleId);
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "META_INF");
            exchange.setProperty("FILES", files);

            exchange.getIn().setBody(metaInfJson);
        });
    }

    private Exchange downloadZipFile(String scanId) {
        RouteEntity route = RouteEntity.builder()
                .id("downloadZipFile")
                .active(true)
                .build();

        return producerTemplate.send("direct:" + route.getId(), exchange ->
                exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId)
        );
    }
}

package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.DataCollectionFileEvent;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.file.GenericFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImportService {

    private final ProducerTemplate producerTemplate;
    private final DataCollectionFileService dataCollectionFileService;

    public ImportService(ProducerTemplate producerTemplate,
                         DataCollectionFileService dataCollectionFileService) {
        this.producerTemplate = producerTemplate;
        this.dataCollectionFileService = dataCollectionFileService;
    }

    public void importData(ImportRequestBO importRequestBO) throws IOException {
        InputStream importStream = importRequestBO.getDataFile().getInputStream();
        String importId = UUID.randomUUID().toString();

        initiateImport(importStream, importId);
    }

    private CompletableFuture<Exchange> initiateImport(InputStream files, String importId) {
        RouteEntity route = RouteEntity.builder()
                .id("importScanData")
                .active(true)
                .build();

        return producerTemplate.asyncSend("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader("IMPORT_ID", importId);
            exchange.getIn().setBody(files);
        });
    }

    public InputStream zip(ZipRequestBO zipRequestBO) throws FileNotFoundException {
        String messagingServiceId = zipRequestBO.getMessagingServiceId();
        String scanId = zipRequestBO.getScanId();

        List<DataCollectionFileEntity> files = dataCollectionFileService.findAllByScanId(scanId);

        String scheduleId = StringUtils.substringBetween(files.stream().findFirst().orElseThrow().getPath(), "/");

        String json = prepareMetaInfJson(files, messagingServiceId, scheduleId, scanId);

        List<DataCollectionFileEvent> fileEvents = prepareFileEvents(files, scanId);

        initiateZip(messagingServiceId, scheduleId, scanId, json, fileEvents);

        Exchange exchange = downloadZipFile(scanId);
        GenericFile<?> downloadedGenericFile = exchange.getIn().getBody(GenericFile.class);
        File downloadedFile = (File) downloadedGenericFile.getFile();

        return new FileInputStream(downloadedFile);
    }

    private String prepareMetaInfJson(List<DataCollectionFileEntity> files, String messagingServiceId,
                                      String scheduleId, String scanId) {

        JSONObject json = new JSONObject();
        JSONArray filesArr = new JSONArray();

        files.forEach(file -> {
            JSONObject fileOj = new JSONObject();
            String filePath = file.getPath();
            String fileName = StringUtils.substringAfterLast(filePath, "/");
            String dataEntityType = fileName.replace(".json", "");
            fileOj.put("fileName", fileName);
            fileOj.put("dataEntityType", dataEntityType);
            filesArr.put(fileOj);
        });

        json.put("messagingServiceId", messagingServiceId);
        json.put("scheduleId", scheduleId);
        json.put("scanId", scanId);
        json.put("files", filesArr);

        return json.toString();
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

    private Exchange initiateZip(String messagingServiceId, String scheduleId, String scanId, String details,
                                 List<DataCollectionFileEvent> files) {
        RouteEntity route = RouteEntity.builder()
                .id("writeMetaInfAndZipFiles")
                .active(true)
                .build();

        return producerTemplate.send("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, scheduleId);
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "META_INF");
            exchange.setProperty("FILES", files);

            exchange.getIn().setBody(details);
        });
    }

    private Exchange downloadZipFile(String scanId) {
        RouteEntity route = RouteEntity.builder()
                .id("downloadZipFile")
                .active(true)
                .build();

        return producerTemplate.send("direct:" + route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
        });
    }
}

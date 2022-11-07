package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatusType;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.DataCollectionFileEvent;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
        InputStream inProgressStatusStream = importRequestBO.getDataFile().getInputStream();
        InputStream completeStatusStream = importRequestBO.getDataFile().getInputStream();
        InputStream importStream = importRequestBO.getDataFile().getInputStream();
        String messagingServiceId = importRequestBO.getMessagingServiceId();
        String scheduleId = importRequestBO.getScheduleId();
        String scanId = importRequestBO.getScanId();

        sendOverAllStatus(inProgressStatusStream, messagingServiceId, scheduleId, scanId, ScanStatus.IN_PROGRESS)
                .thenCompose(exchange -> initiateImport(importStream, messagingServiceId, scheduleId, scanId))
                .thenCompose(exchange -> sendOverAllStatus(completeStatusStream, messagingServiceId, scheduleId, scanId, ScanStatus.COMPLETE));
    }

    private CompletableFuture<Exchange> sendOverAllStatus(InputStream files, String messagingServiceId,
                                                          String scheduleId, String scanId, ScanStatus scanStatus) {
        RouteEntity route = RouteEntity.builder()
                .id("processOverAllImportStatus")
                .active(true)
                .build();

        return producerTemplate.asyncSend("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, scheduleId);
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, scanStatus);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.OVERALL);

            exchange.getIn().setBody(files);
        });
    }

    private CompletableFuture<Exchange> initiateImport(InputStream files, String messagingServiceId,
                                                       String scheduleId, String scanId) {
        RouteEntity route = RouteEntity.builder()
                .id("manualImport")
                .active(true)
                .build();

        return producerTemplate.asyncSend("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, scheduleId);
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);

            exchange.getIn().setBody(files);
        });
    }

    public void zip(ZipRequestBO zipRequestBO) {
        String messagingServiceId = zipRequestBO.getMessagingServiceId();
        String scanId = zipRequestBO.getScanId();

        List<DataCollectionFileEntity> files = dataCollectionFileService.findAllByScanId(scanId);

        List<String> dataFiles = files.stream()
                .map(DataCollectionFileEntity::getPath)
                .collect(Collectors.toUnmodifiableList());

        String scheduleId = StringUtils.substringBetween(dataFiles.stream().findFirst().orElseThrow(), "/");

        JSONObject json = new JSONObject();
        JSONArray filesArr = new JSONArray();

        dataFiles.forEach(filesArr::put);
        json.put("messagingServiceId", messagingServiceId);
        json.put("scheduleId", scheduleId);
        json.put("scanId", scanId);
        json.put("files", filesArr);

        List<DataCollectionFileEvent> events = files.stream()
                .map(file -> {
                    Path path = Paths.get(file.getPath());

                    return DataCollectionFileEvent.builder()
                            .id(file.getId())
                            .name(path.getFileName().toString())
                            .path(path.getParent().toString())
                            .scanId(scanId).purged(false)
                            .build();
                }).collect(Collectors.toUnmodifiableList());

        initiateZip(messagingServiceId, scheduleId, scanId, json.toString(), events);
    }

    private void initiateZip(String messagingServiceId, String scheduleId, String scanId, String details,
                             List<DataCollectionFileEvent> files) {
        RouteEntity route = RouteEntity.builder()
                .id("metaInfFileWrite")
                .active(true)
                .build();

        producerTemplate.send("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, scheduleId);
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "META_INF");
            exchange.setProperty("FILES", files);

            exchange.getIn().setBody(details);
        });
    }
}

package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.constants.RestEndpoint;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RestController
@RequestMapping(RestEndpoint.BASE_URL)
public class DataImportControllerImpl implements DataImportController {

    private final ImportService importService;

    @Autowired
    public DataImportControllerImpl(ImportService importService) {
        this.importService = importService;
    }

    @Override
    @PostMapping(value = "/{messagingServiceId}/import")
    public ResponseEntity<String> read(@PathVariable(value = "messagingServiceId") String messagingServiceId,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "scheduleId", required = false) String scheduleId,
                                       @RequestParam(value = "scanId", required = false) String scanId) {
        try {
            ImportRequestBO importRequestBO = ImportRequestBO.builder()
                    .dataFile(file)
                    .messagingServiceId(messagingServiceId)
                    .scheduleId(scheduleId)
                    .scanId(scanId)
                    .build();

            log.info("Received import request. Request details: {}", importRequestBO);

            importService.importData(importRequestBO);

            String message = String.format("Import complete for schedule: %s, scan request: %s.",
                    scheduleId != null ? scheduleId : "\"\"", scanId != null ? scanId : "\"\"");

            log.info(message);

            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

      @Override
    @PostMapping(value = "/{messagingServiceId}/scans/{scanId}/zip")
    public ResponseEntity<String> zip(@PathVariable(value = "messagingServiceId") String messagingServiceId,
                                      @PathVariable(value = "scanId") String scanId) {
        try {
            ZipRequestBO zipRequestBO = ZipRequestBO.builder()
                    .messagingServiceId(messagingServiceId)
                    .scanId(scanId)
                    .build();

            log.info("Received zip request for scan: {}", scanId);

            importService.zip(zipRequestBO);

            String message = String.format("Zipping complete for scan request: %s.", scanId != null ? scanId : "\"\"");
            log.info(message);

            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}

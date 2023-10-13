package com.solace.maas.ep.event.management.agent.scanManager.rest;

import brave.Tracer;
import com.solace.maas.ep.event.management.agent.constants.RestEndpoint;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping(RestEndpoint.MESSAGING_SERVICE_URL)
public class DataImportControllerImpl implements DataImportController {

    private final Tracer tracer;
    private final ImportService importService;

    @Autowired
    public DataImportControllerImpl(Tracer tracer, ImportService importService) {
        this.tracer = tracer;
        this.importService = importService;
    }

    @Override
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> read(@Parameter(description = "The scan data zip file to be imported.")
                                       @RequestPart("file") final MultipartFile file) {
        try {
            //String traceId = MDC.get("traceId");
            String traceId = tracer.currentSpan().context().traceIdString();
            ImportRequestBO importRequestBO = ImportRequestBO.builder()
                    .dataFile(file)
                    .traceId(traceId)
                    .build();

            log.info("Received import request for data file: {}", importRequestBO.getDataFile().getOriginalFilename());

            importService.importData(importRequestBO);

            String message = "Manual import started.";
            log.info(message);

            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @Override
    @GetMapping(value = "/export/{scanId}/zip")
    @SuppressWarnings("PMD.CloseResource")
    public ResponseEntity<InputStreamResource> zip(@PathVariable(value = "scanId") String scanId) {
        try {
            ZipRequestBO zipRequestBO = ZipRequestBO.builder()
                    .scanId(scanId)
                    .build();
            log.info("Received zip request for scan: {}", scanId);

            InputStream zipInputStream = importService.zip(zipRequestBO);
            InputStreamResource inputStreamResource = new InputStreamResource(zipInputStream);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + scanId + ".zip");

            String message = String.format("Zipping complete for scan request: %s.", scanId != null ? scanId : "\"\"");
            log.info(message);

            return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

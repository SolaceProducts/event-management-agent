package com.solace.maas.ep.event.management.agent.cli;

import com.solace.maas.ep.event.management.agent.cli.compat.CommonsMultipartFile;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Component
@Order(1)
public class EmaCli implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(EmaCli.class);

    private final ScanManager scanManager;
    private final ImportService importService;
    private final IDGenerator idGenerator;

    public EmaCli(ScanManager scanManager, ImportService importService, IDGenerator idGenerator) {
        this.scanManager = scanManager;
        this.importService = importService;
        this.idGenerator = idGenerator;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 1) {
            String type = args[0];
            String body = args[1];

            if ("scan".equals(type)) {
                ScanRequestBO scanRequestBO = new ScanRequestBO();
                scanRequestBO.setMessagingServiceId(body);
                scanRequestBO.setScanId(idGenerator.generateRandomUniqueId());
                scanRequestBO.setScanTypes(List.of("SOLACE_ALL"));
                scanRequestBO.setDestinations(List.of("FILE_WRITER"));

                log.info("Scan request [{}]: Received, request details: {}", scanRequestBO.getScanId(), scanRequestBO);
                String scanId = scanManager.scan(scanRequestBO);
                log.info("Scan request [{}]: Scan started.", scanId);
            } else if ("import".equals(type)) {
                File file = new File(body);
                DiskFileItem fileItem = new DiskFileItem("file", "application/zip", false, file.getName(), (int) file.length() , file.getParentFile());
                fileItem.getOutputStream();
                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

                ImportRequestBO importRequestBO = ImportRequestBO.builder()
                        .dataFile(multipartFile)
                        .traceId(UUID.randomUUID().toString())
                        .build();

                log.info("Received import request for data file: {}", importRequestBO.getDataFile().getOriginalFilename());
                importService.importData(importRequestBO);
            }
        }
    }
}

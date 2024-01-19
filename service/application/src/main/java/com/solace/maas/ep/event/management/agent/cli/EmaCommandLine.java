package com.solace.maas.ep.event.management.agent.cli;

import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import com.solace.maas.ep.event.management.agent.service.DataCollectionFileService;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.ScanStatusService;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Order(1)
@Slf4j
public class EmaCommandLine implements CommandLineRunner {
    private final ScanManager scanManager;
    private final ImportService importService;
    private final ScanStatusService scanStatusService;
    private final IDGenerator idGenerator;
    private final DataCollectionFileService dataCollectionFileService;
    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    public EmaCommandLine(ScanManager scanManager,
                          ImportService importService,
                          ScanStatusService scanStatusService,
                          IDGenerator idGenerator,
                          DataCollectionFileService dataCollectionFileService,
                          MessagingServiceDelegateServiceImpl messagingServiceDelegateService) {
        this.scanManager = scanManager;
        this.importService = importService;
        this.scanStatusService = scanStatusService;
        this.idGenerator = idGenerator;
        this.dataCollectionFileService = dataCollectionFileService;
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 2) {

            String type = args[0];

            if ("scan".equals(type)) {
                String messagingServiceId = args[1];
                String filePathAndName = args[2];

                runScan(messagingServiceId, filePathAndName);
            } else {
                log.error("Unknown command: {}", type);
            }
        } else {
            log.error("Not enough arguments passed to the application.");
        }
    }

    private void runScan(String messagingServiceId, String filePathAndName) throws InterruptedException, IOException {
        MessagingServiceEntity messagingServiceEntity = messagingServiceDelegateService.getMessagingServiceById(messagingServiceId);

        ScanRequestBO scanRequestBO = new ScanRequestBO();
        scanRequestBO.setMessagingServiceId(messagingServiceId);
        scanRequestBO.setScanId(idGenerator.generateRandomUniqueId());
        setScanType(messagingServiceEntity, scanRequestBO, messagingServiceId);
        scanRequestBO.setDestinations(List.of("FILE_WRITER"));

        log.info("Scan request [{}]: Received, request details: {}", scanRequestBO.getScanId(), scanRequestBO);
        String scanId = scanManager.scan(scanRequestBO);

        log.info("Scan request [{}]: Scan started.", scanId);
        waitForScanToComplete(scanId);

        if (isCompletedSuccessfully(scanId)) {
            writeScanToZipFile(filePathAndName, scanId);
        } else {
            log.error("Scan request [{}]: Scan did not complete successfully.", scanId);
        }
    }

    private boolean isCompletedSuccessfully(String scanId) {
        List<ScanStatusEntity> statuses = scanStatusService.getScanStatuses(scanId);
        boolean completedSuccessfully = false;
        if (dataCollectionFileService.findAllByScanId(scanId).size() >=
                statuses.size() && !anyScanStatusesInDesiredState("FAILED", statuses)) {
            log.info("Scan request [{}]: Scan completed successfully.", scanId);
            completedSuccessfully = true;
        }
        return completedSuccessfully;
    }

    private void setScanType(MessagingServiceEntity messagingServiceEntity, ScanRequestBO scanRequestBO, String messagingServiceId) {
        switch (messagingServiceEntity.getType().toLowerCase()) {
            case "solace":
                scanRequestBO.setScanTypes(List.of("SOLACE_ALL"));
                break;
            case "kafka":
                messagingServiceDelegateService.getMessagingServicesRelations(messagingServiceId).stream()
                        .findFirst()
                        .ifPresentOrElse(messagingServiceEntity1 -> scanRequestBO.setScanTypes(List.of("KAFKA_ALL", "CONFLUENT_SCHEMA_REGISTRY_SCHEMA")),
                                () -> scanRequestBO.setScanTypes(List.of("KAFKA_ALL")));
                break;
            case "confluent_schema_registry":
                scanRequestBO.setScanTypes(List.of("CONFLUENT_SCHEMA_REGISTRY_SCHEMA"));
                break;
            default:
                throw new RuntimeException("Unsupported messaging service type: " + messagingServiceEntity.getType());
        }
    }

    public void waitForScanToComplete(String scanId) throws InterruptedException {
        AtomicBoolean scanCompleted = new AtomicBoolean(false);

        // The hard timeout is set to 30 minutes to prevent the scan from running forever
        long hardTimeoutMillis = System.currentTimeMillis() + 30 * 60 * 1000;

        // The initiated timout is set to 2 minutes to shortcut a scan that is stuck in the INITIATED state
        // This typically occurs if there is an error in the broker URL or credentials
        long initiatedTimeoutMillis = System.currentTimeMillis() + 2 * 60 * 1000;

        while (System.currentTimeMillis() < hardTimeoutMillis && !scanCompleted.get()) {
            Thread.sleep(2000);

            List<ScanStatusEntity> statuses = scanStatusService.getScanStatuses(scanId);

            if (scanCompleted(statuses)) {
                scanCompleted.set(true);
            } else if (System.currentTimeMillis() > initiatedTimeoutMillis && scanInitiated(statuses)) {
                throw new RuntimeException("Scan is stuck in INITIATED state. Check broker URL and credentials.");
            } else {
                log.debug("Waiting for scan to complete...");
            }
        }
    }

    private void writeScanToZipFile(String filePathAndName, String scanId) throws IOException {
        // Use the import service to receive the scan stream and write
        // it to a zip file
        ZipRequestBO zipRequestBO = new ZipRequestBO();
        zipRequestBO.setScanId(scanId);
        log.info("Received zip request for scan id: {}", zipRequestBO.getScanId());
        try (InputStream fileStream = importService.zip(zipRequestBO)) {

            File file = new File(filePathAndName);
            writeInputStreamToFile(fileStream, file);
        }
    }

    private static void writeInputStreamToFile(InputStream inputStream, File file) {
        try {
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean scanCompleted(List<ScanStatusEntity> statuses) {
        return scanSucceeded(statuses) || scanFailed(statuses);
    }

    private boolean scanSucceeded(List<ScanStatusEntity> statuses) {
        return allScanStatusesInDesiredState("COMPLETE", statuses);
    }

    private boolean scanFailed(List<ScanStatusEntity> statuses) {
        return anyScanStatusesInDesiredState("FAILED", statuses);
    }

    private boolean scanInitiated(List<ScanStatusEntity> statuses) {
        return allScanStatusesInDesiredState("INITIATED", statuses);
    }

    private boolean allScanStatusesInDesiredState(String desiredState, List<ScanStatusEntity> statuses) {
        // Check that all the scan types have the desired state
        AtomicBoolean allScanTypesDesired = new AtomicBoolean(true);
        statuses.forEach(scanStatusEntity -> {
            if (!desiredState.equals(scanStatusEntity.getStatus())) {
                allScanTypesDesired.set(false);
            }
        });

        return allScanTypesDesired.get();
    }

    private boolean anyScanStatusesInDesiredState(String desiredState, List<ScanStatusEntity> statuses) {
        // Check that all the scan types have the desired state
        AtomicBoolean anyScanTypesDesired = new AtomicBoolean(false);
        statuses.forEach(scanStatusEntity -> {
            if (desiredState.equals(scanStatusEntity.getStatus())) {
                anyScanTypesDesired.set(true);
            }
        });

        return anyScanTypesDesired.get();
    }

}


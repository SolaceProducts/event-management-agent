package com.solace.maas.ep.event.management.agent.cli;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.service.ScanStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class CommandLineCommon {

    private final ScanStatusService scanStatusService;

    public CommandLineCommon(ScanStatusService scanStatusService) {
        this.scanStatusService = scanStatusService;
    }

    public void waitForOperationToComplete(String scanId) throws InterruptedException {
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
                log.debug("Waiting for operation to complete...");
            }
        }
    }

    private boolean scanCompleted(List<ScanStatusEntity> statuses) {
        return scanSucceeded(statuses) || scanFailed(statuses);
    }

    public boolean operationSuccesful(String scanId) {
        return scanSucceeded(scanStatusService.getScanStatuses(scanId));
    }

    public boolean scanSucceeded(List<ScanStatusEntity> statuses) {
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

    public boolean anyScanStatusesInDesiredState(String desiredState, List<ScanStatusEntity> statuses) {
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

package com.solace.maas.ep.event.management.agent.service.lifecycle;

import com.solace.maas.ep.event.management.agent.repository.scan.ScanRepository;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanLifecycleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class ScanLifecycleService {
    private final ScanRepository scanRepository;
    private final Map<String, ScanLifecycleEntity> scanLifecycleEntityMap = new HashMap<>();

    public ScanLifecycleService(ScanRepository scanRepository) {
        this.scanRepository = scanRepository;
    }

    public void addScanLifecycleEntity(ScanLifecycleEntity scanLifecycleEntity) {
        scanLifecycleEntityMap.put(scanLifecycleEntity.getScanId(), scanLifecycleEntity);
    }

    public ScanLifecycleEntity getScanLifecycleEntity(String scanId) {
        return scanLifecycleEntityMap.get(scanId);
    }

    public boolean scanRouteCompleted(String scanId) {
        AtomicReference<Boolean> isScanComplete = new AtomicReference<>(false);
        ScanLifecycleEntity scanLifecycleEntity = scanLifecycleEntityMap.get(scanId);

        if (scanLifecycleEntity != null) {
            scanLifecycleEntity.incrementReceivedCompletionMessagesCount();

            if (scanLifecycleEntity.isScanComplete()) {
                scanRepository.findById(scanId).ifPresent(scanEntity -> {
                    scanEntity.setActive(false);
                    scanRepository.save(scanEntity);
                    isScanComplete.set(true);
                });
            }
        }
        return isScanComplete.get();
    }
}

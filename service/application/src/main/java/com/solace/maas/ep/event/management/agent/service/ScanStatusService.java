package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ScanStatusService {
    private final ScanStatusRepository repository;

    private final ScanTypeService scanTypeService;

    public ScanStatusService(ScanStatusRepository repository, ScanTypeService scanTypeService) {
        this.repository = repository;
        this.scanTypeService = scanTypeService;
    }

    @Transactional
    public ScanStatusEntity save(String name, String scanId) {
        ScanTypeEntity scanType = scanTypeService.findByNameAndScanId(name, scanId)
                .orElseThrow(() -> new RuntimeException("Can't apply Scan Status to Scan that doesn't exist!"));

        ScanStatusEntity scanStatusEntity = scanType.getStatus();

        if(Objects.nonNull(scanStatusEntity)) {
            scanStatusEntity.setStatus(ScanStatus.COMPLETE.name());
        } else {
            scanStatusEntity = ScanStatusEntity.builder()
                    .scanType(scanType)
                    .status(ScanStatus.COMPLETE.name())
                    .build();
        }

        return repository.save(scanStatusEntity);
    }
}

package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanStatusRepository;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ScanStatusService {
    private final ScanStatusRepository repository;

    private final ScanTypeService scanTypeService;
    private final ScanService scanService;

    private final IDGenerator idGenerator;

    public ScanStatusService(ScanStatusRepository repository, ScanTypeService scanTypeService, ScanService scanService, IDGenerator idGenerator) {
        this.repository = repository;
        this.scanTypeService = scanTypeService;
        this.scanService = scanService;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public ScanStatusEntity save(String name, String scanId) {
        return save(name, scanId, ScanStatus.COMPLETE);
    }

    @Transactional
    public ScanStatusEntity save(String name, String scanId, ScanStatus scanStatus) {
        ScanTypeEntity scanType = scanTypeService.findByNameAndScanId(name, scanId)
                .orElseThrow(() -> new RuntimeException("Can't apply Scan Status to Scan that doesn't exist!"));

        ScanStatusEntity scanStatusEntity = scanType.getStatus();

        if (Objects.nonNull(scanStatusEntity)) {
            scanStatusEntity.setStatus(scanStatus.name());
        } else {
            scanStatusEntity = ScanStatusEntity.builder()
                    .id(idGenerator.generateRandomUniqueId())
                    .scanType(scanType)
                    .status(scanStatus.name())
                    .build();
        }

        return repository.save(scanStatusEntity);
    }

    @Transactional
    public List<ScanStatusEntity> getScanStatuses(String scanId) {
        ScanEntity scanEntity = scanService.findById(scanId)
                .orElseThrow(() -> new RuntimeException("Scan not found!"));
        List<ScanStatusEntity> scanStatuses = scanEntity.getScanTypes().stream()
                .map(ScanTypeEntity::getStatus)
                .toList();
        return scanStatuses;
    }
}

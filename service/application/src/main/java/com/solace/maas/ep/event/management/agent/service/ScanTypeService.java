package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScanTypeService {
    private final ScanTypeRepository repository;

    public ScanTypeService(ScanTypeRepository repository) {
        this.repository = repository;
    }

    public ScanTypeEntity save(ScanTypeEntity scanTypeEntity) {
        return repository.save(scanTypeEntity);
    }

    public Optional<ScanTypeEntity> findByNameAndScanId(String name, String scanId) {
        return repository.findByNameAndScanId(name, scanId);
    }

    public Iterable<ScanTypeEntity> saveAll(List<ScanTypeEntity> scanTypeEntities) {
        return repository.saveAll(scanTypeEntities);
    }
}

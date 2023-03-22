package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportDetailsRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportDetailsEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManualImportDetailsService {
    private final ManualImportDetailsRepository manualImportDetailsRepository;

    public ManualImportDetailsService(ManualImportDetailsRepository manualImportDetailsRepository) {
        this.manualImportDetailsRepository = manualImportDetailsRepository;
    }

    public ManualImportDetailsEntity save(ManualImportDetailsEntity manualImportDetailsEntity) {
        return manualImportDetailsRepository.save(manualImportDetailsEntity);
    }

    public Optional<ManualImportDetailsEntity> getByScanId(String scanId) {
        return manualImportDetailsRepository.getByScanId(scanId);
    }
}

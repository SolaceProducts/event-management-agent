package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import org.springframework.stereotype.Service;

@Service
public class ManualImportService {
    private final ManualImportRepository manualImportRepository;

    public ManualImportService(ManualImportRepository manualImportRepository) {
        this.manualImportRepository = manualImportRepository;
    }

    public ManualImportEntity save(ManualImportEntity manualImportEntity) {
        return manualImportRepository.save(manualImportEntity);
    }
}

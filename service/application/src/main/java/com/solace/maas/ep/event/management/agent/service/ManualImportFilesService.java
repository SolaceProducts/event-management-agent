package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportFilesRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportFilesEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManualImportFilesService {
    private final ManualImportFilesRepository manualImportFilesRepository;

    public ManualImportFilesService(ManualImportFilesRepository manualImportFilesRepository) {
        this.manualImportFilesRepository = manualImportFilesRepository;
    }

    public Iterable<ManualImportFilesEntity> saveAll(List<ManualImportFilesEntity> manualImportFilesEntityList) {
        return manualImportFilesRepository.saveAll(manualImportFilesEntityList);
    }

    public List<ManualImportFilesEntity> getAllByScanId(String scanId) {
        return manualImportFilesRepository.getAllByScanId(scanId);
    }
}

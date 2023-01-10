package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.file.DataCollectionFileRepository;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.file.aggregation.AggregatedFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.AggregatedFileEvent;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.DataCollectionFileEvent;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.FileStoreManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.DataCollectionFileBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DataCollectionFileService implements FileStoreManager {
    private final DataCollectionFileRepository repository;

    private final ScanService scanService;

    private final AggregatedFileService aggregatedFileService;

    @Autowired
    public DataCollectionFileService(DataCollectionFileRepository repository, ScanService scanService,
                                     AggregatedFileService aggregatedFileService) {
        this.repository = repository;
        this.scanService = scanService;
        this.aggregatedFileService = aggregatedFileService;
    }

    @Transactional
    @Override
    public void storeRecord(DataCollectionFileEvent dataCollectionFileEvent) {
        String scanId = dataCollectionFileEvent.getScanId();

        ScanEntity scanEntity = scanService.findById(scanId)
                .orElseThrow();

        repository.findByPath(dataCollectionFileEvent.getPath())
                .orElseGet(() -> {
                    DataCollectionFileEntity dataCollectionFileEntity = DataCollectionFileEntity.builder()
                            .path(dataCollectionFileEvent.getPath())
                            .purged(dataCollectionFileEvent.isPurged())
                            .scan(scanEntity)
                            .build();

                    return save(dataCollectionFileEntity);
                });
    }

    @Transactional
    public void storeRecord(AggregatedFileEvent aggregatedFileEvent) {
        Iterable<DataCollectionFileEntity> files = repository.findAllById(aggregatedFileEvent.getFileIds());
        List<DataCollectionFileEntity> fileList = StreamSupport.stream(files.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());

        aggregatedFileService.findByPath(aggregatedFileEvent.getPath())
                .orElseGet(() -> {
                            AggregatedFileEntity aggregatedFileEntity = AggregatedFileEntity.builder()
                                    .files(fileList)
                                    .path(aggregatedFileEvent.getPath())
                                    .purged(false)
                                    .build();

                            return aggregatedFileService.save(aggregatedFileEntity);
                        }
                );
    }

    public DataCollectionFileEntity save(DataCollectionFileEntity dataCollectionFileEntity) {
        return repository.save(dataCollectionFileEntity);
    }

    public List<DataCollectionFileEntity> findAllByScanId(String scanId) {
        return repository.findDataCollectionFileEntitiesByScanId(scanId);
    }

    public Page<DataCollectionFileBO> findByScanId(String scanId, Pageable pageable) {
        return repository.findDataCollectionFileEntitiesByScanId(scanId, pageable)
                .map(dataCollectionFileEntity -> DataCollectionFileBO.builder()
                        .id(dataCollectionFileEntity.getId())
                        .path(dataCollectionFileEntity.getPath())
                        .purged(dataCollectionFileEntity.isPurged())
                        .build());
    }

    public List<DataCollectionFileEntity> findAll() {
        Iterable<DataCollectionFileEntity> files = repository.findAll();
        return StreamSupport.stream(files.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
    }
}

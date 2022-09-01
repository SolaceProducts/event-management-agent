package com.solace.maas.ep.runtime.agent.service;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.repository.file.DataCollectionFileRepository;
import com.solace.maas.ep.runtime.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanEntity;

import com.solace.maas.ep.runtime.agent.plugin.processor.output.file.event.AggregatedFileEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.output.file.event.DataCollectionFileEvent;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class DataCollectionFileServiceTests {
    @Mock
    private DataCollectionFileRepository repository;

    @Mock
    private ScanService scanService;

    @Mock
    private AggregatedFileService aggregatedFileService;

    @InjectMocks
    private DataCollectionFileService dataCollectionFileService;

    @Test
    public void testStoreRecord() {
        String scanId = UUID.randomUUID().toString();

        ScanEntity scanEntity = ScanEntity.builder()
                .id(scanId)
                .scanType("topicListing")
                .active(true)
                .dataCollectionFiles(List.of())
                .build();

        DataCollectionFileEntity dataCollectionFileEntity =
                DataCollectionFileEntity.builder()
                        .path("/some/path")
                        .id(UUID.randomUUID().toString())
                        .scan(scanEntity)
                        .purged(false)
                        .build();

        when(scanService.findById(any(String.class)))
                .thenReturn(Optional.of(scanEntity));
        when(repository.findByPath(any(String.class)))
                .thenReturn(Optional.of(dataCollectionFileEntity));

        dataCollectionFileService.storeRecord(DataCollectionFileEvent.builder()
                .path("/some/path")
                .scanId(scanId)
                .purged(false)
                .build());

        assertThatNoException();
    }

    @Test
    public void testStoreRecordAggregation() {
        AggregatedFileEvent event = AggregatedFileEvent.builder()
                .fileIds(List.of("file1"))
                .purged(false)
                .path("/test/file.json")
                .id(UUID.randomUUID().toString())
                .build();

        when(aggregatedFileService.findByPath(any(String.class)))
                .thenReturn(Optional.empty());

        dataCollectionFileService.storeRecord(event);

        assertThatNoException();
    }

    @Test
    public void testSave() {
        ScanEntity scanEntity = ScanEntity.builder()
                .id(UUID.randomUUID().toString())
                .scanType("topicListing")
                .active(true)
                .dataCollectionFiles(List.of())
                .build();

        DataCollectionFileEntity dataCollectionFileEntity =
                DataCollectionFileEntity.builder()
                        .path("/some/path")
                        .id(UUID.randomUUID().toString())
                        .scan(scanEntity)
                        .purged(false)
                        .build();

        dataCollectionFileService.save(dataCollectionFileEntity);

        assertThatNoException();
    }

    @Test
    public void testFindAllByScanId() {
        when(repository.findDataCollectionFileEntitiesByScanId(any(String.class)))
                .thenReturn(List.of());

        dataCollectionFileService.findAllByScanId("scan1");

        assertThatNoException();
    }

    @Test
    public void testFindAll() {
        when(repository.findAll())
                .thenReturn(List.of(
                        DataCollectionFileEntity.builder()
                                .id(UUID.randomUUID().toString())
                                .path("/test/file.json")
                                .purged(false)
                                .aggregatedFiles(List.of())
                                .build()
                ));

        dataCollectionFileService.findAll();

        assertThatNoException();
    }
}

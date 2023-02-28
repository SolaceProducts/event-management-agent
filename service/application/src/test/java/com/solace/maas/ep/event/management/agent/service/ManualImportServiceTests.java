package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ManualImportServiceTests {

    @Mock
    private ManualImportRepository manualImportRepository;

    @InjectMocks
    private ManualImportService manualImportService;

    @SneakyThrows
    @Test
    public void testSave() {
        ManualImportEntity manualImportEntity = ManualImportEntity.builder()
                .id(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString())
                .fileName("queueListing.json")
                .scanType(ScanTypeEntity.builder().build())
                .build();

        when(manualImportRepository.save(any(ManualImportEntity.class)))
                .thenReturn(manualImportEntity);

        ManualImportEntity result = manualImportService.save(manualImportEntity);

        assertThat(result.getId()).isEqualTo(manualImportEntity.getId());
        assertThat(result.getFileName()).isEqualTo(manualImportEntity.getFileName());
    }
}

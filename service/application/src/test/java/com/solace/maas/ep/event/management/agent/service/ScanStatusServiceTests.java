package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanStatusRepository;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanStatusServiceTests {

    @Mock
    ScanTypeService scanTypeService;

    @Mock
    ScanStatusRepository scanStatusRepository;

    @Mock
    IDGenerator idGenerator;

    @InjectMocks
    private ScanStatusService scanStatusService;

    @SneakyThrows
    @Test
    public void testSaveWithScanStatus() {
        when(scanTypeService.findByNameAndScanId(any(String.class), any(String.class)))
                .thenReturn(Optional.ofNullable(ScanTypeEntity.builder()
                        .id("abc123")
                        .name("queueListing")
                        .scan(ScanEntity.builder().build())
                        .status(ScanStatusEntity.builder()
                                .status(ScanStatus.IN_PROGRESS.name())
                                .build())
                        .build()));

        when(scanStatusRepository.save(any(ScanStatusEntity.class)))
                .thenReturn(ScanStatusEntity.builder().build());

        scanStatusService.save("name", "scanId");

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testSaveWithoutScanStatus() {
        when(scanTypeService.findByNameAndScanId(any(String.class), any(String.class)))
                .thenReturn(Optional.ofNullable(ScanTypeEntity.builder()
                        .id("abc123")
                        .name("queueListing")
                        .scan(ScanEntity.builder().build())
                        .build()));

        when(idGenerator.generateRandomUniqueId())
                .thenReturn("abc123");

        when(scanStatusRepository.save(any(ScanStatusEntity.class)))
                .thenReturn(ScanStatusEntity.builder().build());

        scanStatusService.save("name", "scanId");

        assertThatNoException();
    }
}

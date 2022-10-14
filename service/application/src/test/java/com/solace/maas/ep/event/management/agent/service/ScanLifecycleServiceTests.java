package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanLifecycleEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRepository;
import com.solace.maas.ep.event.management.agent.service.lifecycle.ScanLifecycleService;
import com.solace.maas.ep.event.management.agent.TestConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanLifecycleServiceTests {

    @InjectMocks
    ScanLifecycleService scanLifecycleService;

    @Mock
    private ScanRepository scanRepository;

    @SneakyThrows
    @Test
    public void testScanLifecycleService() throws Exception {
        Map<String, ScanLifecycleEntity> scanLifecycleEntityMap = new HashMap<>();
        ScanLifecycleEntity scanLifecycleEntity = mock(ScanLifecycleEntity.class);

        ScanEntity scanEntity = ScanEntity.builder()
                .id(UUID.randomUUID().toString())
                .scanType("")
                .active(true)
                .dataCollectionFiles(List.of())
                .build();

        scanLifecycleEntityMap.put("scanId", scanLifecycleEntity);

        when(scanLifecycleEntity.getScanId())
                .thenReturn("scanId");
        when(scanRepository.findById(any(String.class)))
                .thenReturn(Optional.of(scanEntity));
        when(scanLifecycleEntity.isScanComplete())
                .thenReturn(true);

        scanLifecycleService.addScanLifecycleEntity(scanLifecycleEntity);
        scanLifecycleService.getScanLifecycleEntity("scanId");
        scanLifecycleService.scanRouteCompleted("scanId");

        assertThatNoException();
    }
}
package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.async.manager.AsyncProcessManager;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import com.solace.maas.ep.event.management.agent.repository.model.scan.AsyncScanProcessEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.AsyncScanProcessRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Slf4j
public class AsyncStorageServiceTests {
    @Mock
    AsyncScanProcessRepository repository;

    @Mock
    AsyncProcessManager asyncProcessManager;

    @InjectMocks
    AsyncStorageService asyncStorageService;

    @Test
    @SneakyThrows
    public void testStoreAsync() {
        AsyncWrapper asyncWrapper = mock(AsyncWrapper.class);

        when(repository.save(any(AsyncScanProcessEntity.class)))
                .thenReturn(AsyncScanProcessEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .active(true)
                        .scanId("scan1")
                        .scanType("testScan")
                        .build());

        asyncStorageService.storeAsync(asyncWrapper, "scan1", "testScan");

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testStopAsync() {
        doNothing().when(asyncProcessManager).terminate(any(String.class));

        when(repository.findAsyncScanProcessEntityByScanIdAndScanType(any(String.class), any(String.class)))
                .thenReturn(
                        Optional.of(
                            AsyncScanProcessEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .active(true)
                            .scanId("scan1")
                            .scanType("testScan")
                            .build()
                        )
                );

        assertThatNoException();
    }
}

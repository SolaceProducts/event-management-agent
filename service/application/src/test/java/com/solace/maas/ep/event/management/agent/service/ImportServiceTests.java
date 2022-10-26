package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("CPD-START")
public class ImportServiceTests {

    @Mock
    private ProducerTemplate producerTemplate;

    @InjectMocks
    private ImportService importService;

    @Test
    public void testSingleScanWithRouteBundle() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("file.tmp", "test".getBytes());

        ImportRequestBO importRequestBO = ImportRequestBO.builder()
                .messagingServiceId("service1")
                .dataFile(multipartFile)
                .scheduleId("scheduleId")
                .scanId("scanId")
                .build();

        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        importService.importData(importRequestBO);

        assertThatNoException();
    }
}

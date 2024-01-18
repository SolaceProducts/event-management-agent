package com.solace.maas.ep.event.management.agent.cli;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.service.DataCollectionFileService;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles({"SCANTEST", "TEST"})
@SpringBootTest(classes = TestConfig.class)
public class CliScanTest {

    @Autowired
    private EmaCommandLine emaCommandLine;

    @Autowired
    private ScanService scanService;

    @Autowired
    private ImportService importService;

    @Autowired
    private DataCollectionFileService dataCollectionFileService;

    @Test
    public void testCLICommand() throws Exception {
        when(scanService.findById(any())).thenReturn(Optional.of(ScanEntity.builder()
                .id("abcdef")
                .messagingService(MessagingServiceEntity.builder()
                        .id("bbbbbb")
                        .build())
                .build()));
        when(dataCollectionFileService.findAllByScanId(any())).thenReturn(
                List.of(DataCollectionFileEntity.builder()
                        .path("a/b/c")
                        .build()));
        when(importService.zip(any())).thenReturn(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)));
        emaCommandLine.run("scan", "abcdef", "/tmp/scan.txt");
    }
}
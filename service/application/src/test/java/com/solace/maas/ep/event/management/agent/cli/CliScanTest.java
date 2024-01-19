package com.solace.maas.ep.event.management.agent.cli;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import com.solace.maas.ep.event.management.agent.service.DataCollectionFileService;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach

    public void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(EmaCommandLine.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    public void testCLICommand() throws Exception {
        when(scanService.singleScan(any(), any(), any(), any(), any(), any(MessagingServiceEntity.class), anyString())).thenReturn("xyz");
        when(scanService.findById(anyString())).thenReturn(Optional.of(ScanEntity.builder()
                .id("abcdef")
                .messagingService(MessagingServiceEntity.builder()
                        .id("bbbbbb")
                        .build())
                .build()));
        when(dataCollectionFileService.findAllByScanId(anyString())).thenReturn(
                List.of(DataCollectionFileEntity.builder()
                        .path("a/b/c")
                        .build()));
        String fileContent = "test";
        when(importService.zip(any(ZipRequestBO.class))).thenReturn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));

        String scanOutputFile = "/tmp/scan.txt";
        emaCommandLine.run("scan", "abcdef", scanOutputFile);

        List<ILoggingEvent> logsList = listAppender.list;
        assertTrue(logsList.get(1).getFormattedMessage().contains("Scan request [xyz]: Scan started."));
        assertTrue(logsList.get(2).getFormattedMessage().contains("Scan request [xyz]: Scan completed successfully."));
        assertTrue(logsList.get(3).getFormattedMessage().contains("Received zip request for scan id: xyz"));

        String content = Files.readString(Path.of(scanOutputFile), StandardCharsets.UTF_8);
        assertEquals(fileContent, content);
    }

    @Test
    public void testCLICommandMissingParams() throws Exception {
        when(scanService.findById(any())).thenReturn(Optional.empty());
        emaCommandLine.run("scan", "abcdef");

        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Not enough arguments passed to the application."));
    }
}
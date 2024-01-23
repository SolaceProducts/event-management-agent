package com.solace.maas.ep.event.management.agent.cli;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ActiveProfiles({"CLITEST", "TEST"})
@SpringBootTest(classes = TestConfig.class, properties = {"spring.main.web-application-type=none",
        "springdoc.api-docs.enabled=false"})
public class CliImportTest {

    @Autowired
    private EmaCommandLine emaCommandLine;

    @Autowired
    private ImportService importService;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach

    public void setUp() {
        Logger importLogger = (Logger) LoggerFactory.getLogger(CommandLineImport.class);
        Logger cliLogger = (Logger) LoggerFactory.getLogger(EmaCommandLine.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        importLogger.addAppender(listAppender);
        cliLogger.addAppender(listAppender);
    }

    @Test
    public void testImportCLICommand() throws Exception {
        doNothing().when(importService).importData(any(ImportRequestBO.class));

        // Read the "scanFile/scan.zip" file from the test resources directory
        String scanFilesPath = StringUtils.join(List.of("src", "test", "resources", "scanFiles"), File.separator);
        File resourcesDirectory = new File(scanFilesPath);
        File stringFile = new File(resourcesDirectory.getAbsolutePath() + File.separator + "scan.zip");

        emaCommandLine.run("upload", stringFile.getAbsolutePath());

        List<ILoggingEvent> logsList = listAppender.list;
        assertTrue(logsList.get(0).getFormattedMessage().contains("Import started."));
    }

    @Test
    public void testImportCLICommandMissingArgs() throws Exception {
        //when(scanService.findById(any())).thenReturn(Optional.empty());
        emaCommandLine.run("upload");
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Not enough arguments passed to the application."));
    }

    @Test
    public void testCLIBadCommand() throws Exception {
        //when(scanService.findById(any())).thenReturn(Optional.empty());
        emaCommandLine.run("flamingo");
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Unknown command: flamingo"));
    }

    @Test
    public void testImportCLIMissingFile() {
        //when(scanService.findById(any())).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () ->
                emaCommandLine.run("upload", "/tmp/missingFile.zip"));
        assertTrue(exception.getMessage().contains("java.nio.file.NoSuchFileException: /tmp/missingFile.zip"));
    }

    @Test
    public void testImportCLINotZipFile() {
        //when(scanService.findById(any())).thenReturn(Optional.empty());
        String scanFilesPath = StringUtils.join(List.of("src", "test", "resources", "scanFiles"), File.separator);
        File resourcesDirectory = new File(scanFilesPath);
        File stringFile = new File(resourcesDirectory.getAbsolutePath() + File.separator + "notAScanZip.zip");
        Exception exception = assertThrows(RuntimeException.class, () ->
                emaCommandLine.run("upload", stringFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("This does not appear to be a scan zip file."));
    }

}
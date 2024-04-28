package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.CommandLogMessage;
import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.mapper.CommandMapper;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.publisher.CommandLogsPublisher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class CommandLogStreamProcessorTest {


    public static final String CMD_CRRLTN_ID = "cmdCrrltnId";

    @Autowired
    private EventPortalProperties eventPortalProperties;

    @Autowired
    @Qualifier("realCommandLogStreamingProcessor")
    private CommandLogStreamingProcessor realCommandLogStreamingProcessor;

    @Autowired
    private CommandLogsPublisher commandLogsPublisher;

    @Autowired
    private CommandMapper commandMapper;


    private Command applyCommand;
    private Command writeHclForImport;
    private Command writeHclForConfig;
    private Command syncCommand;

    @BeforeEach
    void setUp() {

        this.writeHclForConfig = Command.builder()
                .commandType(CommandType.terraform)
                .ignoreResult(false)
                .body("some data")
                .command("write_HCL")
                .build();

        this.writeHclForImport = Command.builder()
                .commandType(CommandType.terraform)
                .ignoreResult(false)
                .body("some data")
                .command("write_HCL")
                .build();

        this.syncCommand = Command.builder()
                .commandType(CommandType.terraform)
                .ignoreResult(true)
                .body("some data")
                .command("sync")
                .build();

        this.applyCommand = Command.builder()
                .commandType(CommandType.terraform)
                .ignoreResult(false)
                .body("some data")
                .command("apply")
                .build();

        reset(commandLogsPublisher);
    }

    @Test
    void testStreamLogsToEPSuccessCase() throws IOException {
        Path applyCommandLog = Path.of(
                ResourceUtils.getFile("classpath:commandLogs" + File.separator + "applyCommandExecutionLog.log").toURI()
        );


        CommandMessage msg = buildCommandMessageForConfigPush(
                List.of(writeHclForImport, writeHclForConfig, syncCommand, applyCommand)
        );

        ArgumentCaptor<CommandLogMessage> logMopCaptor = ArgumentCaptor.forClass(CommandLogMessage.class);
        applyCommand.setResult(
                CommandResult.builder()
                        .status(JobStatus.success)
                        .build()
        );
        realCommandLogStreamingProcessor.streamLogsToEP(
                commandMapper.map(msg),
                applyCommand,
                applyCommandLog
        );
        // Only change_summary type logs will be sent if  command is successful
        verify(commandLogsPublisher, times(2)).sendCommandLogData(logMopCaptor.capture(), any());
    }

    @Test
    void testStreamLogsToEPErrorCase() throws IOException {

        CommandMessage msg = buildCommandMessageForConfigPush(
                List.of(writeHclForImport, writeHclForConfig, syncCommand, applyCommand)
        );
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->

                realCommandLogStreamingProcessor.streamLogsToEP(
                        commandMapper.map(msg),
                        applyCommand,
                        null)
        );

        Assertions.assertThat(thrown.getMessage()).isEqualTo(
                String.format("Execution log was not found for command %s with commandCorrelationId %s", applyCommand.getCommand(), CMD_CRRLTN_ID)
        );

        Path applyCommandLog = Path.of(
                ResourceUtils.getFile("classpath:commandLogs" + File.separator + "applyCommandExecutionLog.log").toURI()
        );


        ArgumentCaptor<CommandLogMessage> logMopCaptor = ArgumentCaptor.forClass(CommandLogMessage.class);
        applyCommand.setResult(
                CommandResult.builder()
                        .status(JobStatus.error)
                        .build()
        );
        realCommandLogStreamingProcessor.streamLogsToEP(
                commandMapper.map(msg),
                applyCommand,
                applyCommandLog
        );
        // All the logs will be sent if command status is error
        verify(commandLogsPublisher, times(52)).sendCommandLogData(logMopCaptor.capture(), any());

    }

    @Test
    void testExecutionLogDeletionSuccessFlow(@TempDir Path logPath) throws IOException {
        Path commandLog1 = logPath.resolve("log1");
        Path commandLog2 = logPath.resolve("log2");
        Path commandLog3 = logPath.resolve("log3");
        Path commandLog4 = logPath.resolve("log4");

        Files.writeString(commandLog1, "log 1");
        Files.writeString(commandLog2, "log 2");
        Files.writeString(commandLog3, "log 3");
        Files.writeString(commandLog4, "log 4");
        List<Path> allLogs = List.of(commandLog1, commandLog2, commandLog3, commandLog4);

        Assertions.assertThat(
                allLogs.stream().allMatch(path -> Files.exists(path, LinkOption.NOFOLLOW_LINKS))
        ).isTrue();
        realCommandLogStreamingProcessor.deleteExecutionLogFiles(
                List.of(commandLog1, commandLog2, commandLog3, commandLog4)
        );

        Assertions.assertThat(
                allLogs.stream().noneMatch(path -> Files.exists(path, LinkOption.NOFOLLOW_LINKS))
        ).isTrue();
    }

    @Test
    void testExecutionLogDeletionWhenSomeLogFilesDontExist(@TempDir Path logPath) throws IOException {
        Path commandLog1 = logPath.resolve("log1");
        Path commandLog2 = logPath.resolve("log2");
        Path commandLog3 = logPath.resolve("log3");
        Path commandLog4 = logPath.resolve("log4");

        Files.writeString(commandLog1, "log 1");
        Files.writeString(commandLog2, "log 2");

        List<Path> allLogs = List.of(commandLog1, commandLog2, commandLog3, commandLog4);

        // Only 2 of the log files exist
        Assertions.assertThat(
                Stream.of(commandLog1, commandLog2).allMatch(path -> Files.exists(path, LinkOption.NOFOLLOW_LINKS))
        ).isTrue();
        /* Although only 2 out of 4 log files exist, the 2 log files will be deleted anyway
         and the errors will be handled gracefully
         */
        realCommandLogStreamingProcessor.deleteExecutionLogFiles(
                List.of(commandLog1, commandLog2, commandLog3, commandLog4)
        );

        Assertions.assertThat(
                allLogs.stream().noneMatch(path -> Files.exists(path, LinkOption.NOFOLLOW_LINKS))
        ).isTrue();

    }

    private CommandMessage buildCommandMessageForConfigPush(List<Command> commands) {

        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId("someId");
        message.setActorId("myActorId");
        message.setOrgId(eventPortalProperties.getOrganizationId());
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId(CMD_CRRLTN_ID);
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(true)
                        .commands(commands)
                        .build()));
        return message;
    }
}

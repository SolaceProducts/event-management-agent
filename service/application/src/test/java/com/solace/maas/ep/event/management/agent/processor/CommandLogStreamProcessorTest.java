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
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestPropertySource(properties = {"event-portal.gateway.messaging.standalone=false", "event-portal.managed=false"})
public class CommandLogStreamProcessorTest {


    public static final String CMD_CRRLTN_ID = "cmdCrrltnId";

    @Autowired
    private EventPortalProperties eventPortalProperties;

    @MockitoSpyBean
    private  CommandLogStreamingProcessor  realCommandLogStreamingProcessor;

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

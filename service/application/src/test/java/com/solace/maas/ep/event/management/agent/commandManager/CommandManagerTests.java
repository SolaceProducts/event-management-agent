package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.command.SempDeleteCommandManager;
import com.solace.maas.ep.event.management.agent.command.mapper.CommandMapper;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.processor.CommandLogStreamingProcessor;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.solace.maas.ep.event.management.agent.constants.Command.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.TRACE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestPropertySource(properties = {"event-portal.gateway.messaging.standalone=false", "event-portal.managed=false"})
class CommandManagerTests {

    @MockitoSpyBean
    private CommandManager commandManager;

    @Autowired
    private CommandMapper commandMapper;

    @MockitoSpyBean
    private SempDeleteCommandManager sempDeleteCommandManager;

    @MockitoSpyBean
    private TerraformManager terraformManager;


    @Autowired
    private CommandPublisher commandPublisher;

    @MockitoSpyBean
    private CommandLogStreamingProcessor commandLogStreamingProcessor;

    @Autowired
    private MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    private EventPortalProperties eventPortalProperties;

    private static final String MESSAGING_SERVICE_ID = "myMessagingServiceId";
    private final static ThreadPoolTaskExecutor testThreadPool = new ThreadPoolTaskExecutor();

    @BeforeEach
    public void cleanup() {
        reset(terraformManager);
        doNothing().when(terraformManager).deleteTerraformState(any());
        reset(commandPublisher);
        reset(commandManager);
        reset(sempDeleteCommandManager);
        reset(messagingServiceDelegateService);
        reset(commandLogStreamingProcessor);
    }

    @Test
    void testMultiThreadedCommandManager() throws InterruptedException {

        // Set up the thread pool
        int commandAmount = 10;


        // Build enough requests to fill the command thread pool queue
        List<CommandMessage> messageList = new ArrayList<>();
        for (int i = 0; i < commandAmount; i++) {
            messageList.add(getCommandMessage(Integer.toString(i)));
        }

        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        doAnswer(invocation -> {
            // Simulate the time spent for a SEMP command to complete
            TimeUnit.SECONDS.sleep(1);
            return null;
        }).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<Map<String, String>> topicArgCaptor = ArgumentCaptor.forClass(Map.class);

        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));

        messageList.stream().forEach(message -> {
            commandManager.execute(message);
        });


        // Verify terraform manager is called
        ArgumentCaptor<Map<String, String>> envArgCaptor = ArgumentCaptor.forClass(Map.class);
        verify(terraformManager, times(commandAmount)).execute(any(), any(), envArgCaptor.capture());

        // Verify the env vars are set with the terraform manager is called
        Map<String, String> envVars = envArgCaptor.getValue();
        assert envVars.get("TF_VAR_password").equals("myPassword");
        assert envVars.get("TF_VAR_username").equals("myUsername");
        assert envVars.get("TF_VAR_url").equals("myConnectionUrl");

        ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(commandAmount)).sendCommandResponse(messageArgCaptor.capture(), topicArgCaptor.capture());

        Map<String, String> topicVars = topicArgCaptor.getValue();
        assert topicVars.get("orgId").equals(eventPortalProperties.getOrganizationId());
        assert topicVars.get("runtimeAgentId").equals(eventPortalProperties.getRuntimeAgentId());

        // Make sure we get all 10 correlation ids in the response messages
        List<String> receivedCorrelationIds = messageArgCaptor.getAllValues().stream().map(CommandMessage::getCommandCorrelationId).toList();
        List<String> expectedCorrelationIds = IntStream.range(0, commandAmount).mapToObj(i -> "myCorrelationId" + i).toList();
        assertTrue(receivedCorrelationIds.size() == expectedCorrelationIds.size() &&
                receivedCorrelationIds.containsAll(expectedCorrelationIds) && expectedCorrelationIds.containsAll(receivedCorrelationIds));
    }

    @Test
    void failSendingResponseBackToEp() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");
        doReturn(Path.of("/some/path/on/disk")).when(terraformManager).execute(any(), any(), any());
        doThrow(new RuntimeException("Error sending response back to EP")).when(commandPublisher).sendCommandResponse(any(), any());
        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));
        doNothing().when(commandLogStreamingProcessor).streamLogsToEP(any(), any(), any());

        commandManager.execute(message);

        ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        // 1 attempt to send the response back to EP
        verify(commandPublisher, times(1)).sendCommandResponse(messageArgCaptor.capture(), any());

        // Check that we attempted to set Error in the response message
        messageArgCaptor.getAllValues().forEach(commandMessage -> {
            assert commandMessage.getCommandCorrelationId().equals(message.getCommandCorrelationId());
            assert commandMessage.getCommandBundles().get(0).getCommands().get(0).getResult().getStatus().equals(JobStatus.error);
        });
    }

    @Test
    void failSettingBrokerSpecificEnvironmentVariables() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");

        doReturn(Path.of("/some/path/on/disk")).when(terraformManager).execute(any(), any(), any());
        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        doThrow(new RuntimeException("Could not retrieve or create the messaging service client for [" + MESSAGING_SERVICE_ID + "]."))
                .when(messagingServiceDelegateService).getMessagingServiceClient(MESSAGING_SERVICE_ID);

        commandManager.execute(message);
        CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1);

        ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(1)).sendCommandResponse(messageArgCaptor.capture(), any());

        assertEquals(JobStatus.error, messageArgCaptor.getValue().getStatus());
    }

    /**
     * @Test void failConfigPushCommand() {
     * // Create a command request message
     * CommandMessage message = getCommandMessage("1");
     * <p>
     * doNothing().when(commandPublisher).sendCommandResponse(any(), any());
     * doThrow(new RuntimeException("Error running command.")).when(commandManager).configPush(any());
     * <p>
     * commandManager.execute(message);
     * CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1);
     * <p>
     * ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
     * verify(commandPublisher, times(1)).sendCommandResponse(messageArgCaptor.capture(), any());
     * <p>
     * assertEquals(JobStatus.error, messageArgCaptor.getValue().getStatus());
     * }
     */

    @Test
    void testCommandManager() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");

        doReturn(Path.of("/some/path/on/disk")).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<Map<String, String>> topicArgCaptor = ArgumentCaptor.forClass(Map.class);
        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));

        commandManager.execute(message);

        CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1);

        // Verify terraform manager is called
        ArgumentCaptor<Map<String, String>> envArgCaptor = ArgumentCaptor.forClass(Map.class);
        verify(terraformManager, times(1)).execute(any(), any(), envArgCaptor.capture());

        // Verify the env vars are set with the terraform manager is called
        Map<String, String> envVars = envArgCaptor.getValue();
        assert envVars.get("TF_VAR_password").equals("myPassword");
        assert envVars.get("TF_VAR_username").equals("myUsername");
        assert envVars.get("TF_VAR_url").equals("myConnectionUrl");

        ArgumentCaptor<CommandMessage> responseCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(1)).sendCommandResponse(responseCaptor.capture(), topicArgCaptor.capture());

        Map<String, String> topicVars = topicArgCaptor.getValue();
        assert topicVars.get("orgId").equals(eventPortalProperties.getOrganizationId());
        assert topicVars.get("runtimeAgentId").equals(eventPortalProperties.getRuntimeAgentId());
        assert topicVars.get(COMMAND_CORRELATION_ID).equals(message.getCommandCorrelationId());

        assertEquals(JobStatus.success, responseCaptor.getValue().getStatus());
    }

    // configPush tries to delete tf-state directory for the given context before executing the commands.
    // If the directory does not exist, it should not throw an exception.
    // This test verifies that unexpected exceptions are caught and the error is reported back to EP
    // The caught exception is attached to the result of the first command in the bundle and the status is set to error
    @Test
    void failCommandManagerConfigPushDeleteTfState() {
        CommandMessage message = getCommandMessage("1");
        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));
        doThrow(new IllegalStateException("Failed removing Terraform state directory")).when(terraformManager).deleteTerraformState(any());
        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        commandManager.execute(message);
        ArgumentCaptor<Map<String, String>> topicVarsCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<CommandMessage> responseCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(1)).sendCommandResponse(responseCaptor.capture(), topicVarsCaptor.capture());
        assertEquals(JobStatus.error, responseCaptor.getValue().getStatus());
        assertEquals("Failed removing Terraform state directory",
                responseCaptor.getValue().getCommandBundles().get(0).getCommands().get(0).getResult().getLogs().get(0).get("message"));
    }

    @Test
    void testCommandManagerConfigPushDeleteTfState() {
        CommandMessage message = getCommandMessage("1");
        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));
        doReturn(Path.of("/some/path/on/disk")).when(terraformManager).execute(any(), any(), any());
        doNothing().when(terraformManager).deleteTerraformState(any());

        commandManager.execute(message);
        ArgumentCaptor<Map<String, String>> topicVarsCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<CommandMessage> responseCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(1)).sendCommandResponse(responseCaptor.capture(), topicVarsCaptor.capture());
        assertEquals(JobStatus.success, responseCaptor.getValue().getStatus());

    }

    @Test
    void verifyExitOnFailureIsRespectedWhenTrueAndIgnoreResultIsFalse() {
        CommandMessage message = getCommandMessage("1", 2, 4, true, false);

        doThrow(new RuntimeException("Error executing command")).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<MOPMessage> mopMessageCaptor = executeCommandAndGetResponseMessage(message);

        CommandMessage commandMessage = (CommandMessage) mopMessageCaptor.getValue();

        // Check top level status
        assertEquals(JobStatus.error, commandMessage.getStatus());
        CommandBundle commandBundle = commandMessage.getCommandBundles().get(0);
        // The first command in the bundle should be marked with error
        assertEquals(JobStatus.error, commandBundle.getCommands().get(0).getResult().getStatus());
        // The rest of the commands should not be executed and have null results
        assertNull(commandBundle.getCommands().get(1).getResult());
        assertNull(commandBundle.getCommands().get(2).getResult());
        assertNull(commandBundle.getCommands().get(3).getResult());
        assertNull(commandBundle.getCommands().get(4).getResult());
        assertNull(commandBundle.getCommands().get(5).getResult());
    }

    @Test
    void verifyExitOnFailureIsRespectedWhenTrueAndIgnoreResultIsTrue() {
        CommandMessage message = getCommandMessage("1", 2, 4, true, true);

        doThrow(new RuntimeException("Error executing command")).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<MOPMessage> mopMessageCaptor = executeCommandAndGetResponseMessage(message);

        CommandMessage commandMessage = (CommandMessage) mopMessageCaptor.getValue();

        // top level status should be success since we are ignoring the result of each command
        assertEquals(JobStatus.success, commandMessage.getStatus());
        verifyAllCommandsHaveErrorState(commandMessage);
    }

    private static void verifyAllCommandsHaveErrorState(CommandMessage commandMessage) {
        CommandBundle commandBundle = commandMessage.getCommandBundles().get(0);
        commandBundle.getCommands().forEach(command -> {
            assertEquals(JobStatus.error, command.getResult().getStatus());
        });
    }

    @Test
    void verifyExitOnFailureIsRespectedWhenFalseAndIgnoreResultIsSetToFalse() {
        CommandMessage message = getCommandMessage("1", 2, 4, false, false);

        doThrow(new RuntimeException("Error executing command")).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<MOPMessage> mopMessageCaptor = executeCommandAndGetResponseMessage(message);

        CommandMessage commandMessage = (CommandMessage) mopMessageCaptor.getValue();

        // Check top level status
        assertEquals(JobStatus.error, commandMessage.getStatus());
        verifyAllCommandsHaveErrorState(commandMessage);
    }


    @Test
    void verifyMDCIsSetInCommandManagerThread() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");
        AtomicBoolean mdcIsSet = new AtomicBoolean(false);
        doAnswer(invocation -> {
            Map<String, String> mdcContextMap = MDC.getCopyOfContextMap();
            String commandCorrelationId = mdcContextMap.get(COMMAND_CORRELATION_ID);
            String traceId = mdcContextMap.get(TRACE_ID);
            if (commandCorrelationId.equals(message.getCommandCorrelationId()) &&
                    traceId.equals(message.getTraceId())) {
                mdcIsSet.set(true);
            }
            return null;
        }).when(terraformManager).execute(any(), any(), any());

        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));

        MDC.put(COMMAND_CORRELATION_ID, message.getCommandCorrelationId());
        MDC.put(TRACE_ID, message.getTraceId());
        commandManager.execute(message);

        // Wait for the command thread to complete
        await().atMost(10, TimeUnit.SECONDS).until(() ->
                CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1));

        assertTrue(mdcIsSet.get());
    }

    private CommandMessage getCommandMessage(String correlationIdSuffix) {
        return getCommandMessage(correlationIdSuffix, 1, false, false);
    }

    private CommandMessage getCommandMessage(String correlationIdSuffix, int numberOfTfCommands,
                                             boolean exitOnFailure, boolean ignoreResult) {
        return getCommandMessage(correlationIdSuffix, 0, numberOfTfCommands, exitOnFailure, ignoreResult);
    }

    private CommandMessage getCommandMessage(String correlationIdSuffix, int numberOfSempDeleteCommands, int numberOfTfCommands,
                                             boolean exitOnFailure, boolean ignoreResult) {
        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId(MESSAGING_SERVICE_ID);
        message.setActorId("myActorId");
        message.setOrgId(eventPortalProperties.getOrganizationId());
        message.setOriginOrgId(eventPortalProperties.getOrganizationId());
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId("myCorrelationId" + correlationIdSuffix);
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(exitOnFailure)
                        .commands(Stream.concat(
                                buildTfCommands(numberOfTfCommands, ignoreResult).stream(),
                                buildSempDeleteCommands(numberOfSempDeleteCommands, ignoreResult).stream()
                        ).toList())
                        .build()));
        return message;
    }

    private static List<Command> buildSempDeleteCommands(int numberOfCommands, boolean ignoreResult) {
        return IntStream.range(0, numberOfCommands).mapToObj(i -> buildSempDeleteCommand(ignoreResult)).toList();
    }

    private static List<Command> buildTfCommands(int numberOfCommands, boolean ignoreResult) {
        return IntStream.range(0, numberOfCommands).mapToObj(i -> buildTfCommand(ignoreResult)).toList();
    }

    private static Command buildSempDeleteCommand(boolean ignoreResult) {
        return Command.builder()
                .commandType(CommandType.semp)
                .ignoreResult(ignoreResult)
                .body("someSempCommandPayload")
                .command("delete")
                .build();
    }

    private static Command buildTfCommand(boolean ignoreResult) {
        return Command.builder()
                .commandType(CommandType.terraform)
                .ignoreResult(ignoreResult)
                .body("asdfasdfadsf")
                .command("apply")
                .build();
    }

    private ArgumentCaptor<MOPMessage> executeCommandAndGetResponseMessage(CommandMessage message) {
        ArgumentCaptor<MOPMessage> mopMessageCaptor = ArgumentCaptor.forClass(MOPMessage.class);

        doNothing().when(commandPublisher).sendCommandResponse(mopMessageCaptor.capture(), any());
        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));

        commandManager.execute(message);

        // Wait for the command thread to complete
        await().atMost(10, TimeUnit.SECONDS).until(() -> CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1));
        return mopMessageCaptor;
    }

    @Nested
    class LogStreamingToEpTest {
        private ArgumentCaptor<List<Path>> executionLogFileCaptor;
        private ArgumentCaptor<Map<String, String>> topicArgCaptor;
        private ArgumentCaptor<Map<String, String>> envArgCaptor;
        private ArgumentCaptor<CommandMessage> responseCaptor;

        private CommandMessage message;

        @BeforeEach
        void setUp() {
            message = CommandManagerTestHelper.buildCommandMessageForConfigPush(eventPortalProperties.getOrganizationId(), MESSAGING_SERVICE_ID);
            executionLogFileCaptor = ArgumentCaptor.forClass(List.class);
            topicArgCaptor = ArgumentCaptor.forClass(Map.class);
            doNothing().when(commandPublisher).sendCommandResponse(any(), any());
            when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                    new SolaceHttpSemp(SempClient.builder()
                            .username("myUsername")
                            .password("myPassword")
                            .connectionUrl("myConnectionUrl")
                            .build()));
            envArgCaptor = ArgumentCaptor.forClass(Map.class);
            responseCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        }

        @Test
        void testLogStreamingToEP(@TempDir Path basePath) throws IOException {

            doAnswer((Answer<Path>) invocation -> {
                Command command = (Command) invocation.getArgument(1);
                return setCommandStatusAndReturnExecutionLog(command, JobStatus.success, true, basePath);
            }).when(terraformManager).execute(any(), any(), any());


            /*
              all 4 commands are executed successfully and all 4 logs are streamed + cleaned
             */

            executeCommandsAndVerify(4, 4);
            verify(commandManager, times(1)).deleteExecutionLogFiles(executionLogFileCaptor.capture());
            Assertions.assertThat(executionLogFileCaptor.getValue())
                    .containsExactlyInAnyOrder(
                            basePath.resolve("apply"),
                            basePath.resolve("write_HCL"),
                            basePath.resolve("write_HCL"),
                            basePath.resolve("sync"));
        }

        @Test
        void testExecutionLogCleanupWhenOneOfTheExecutionLogPathIsNull(@TempDir Path basePath) {
            doAnswer((Answer<Path>) invocation -> {
                Command command = (Command) invocation.getArgument(1);
                // intentionally making the sync command return null path to test graceful cleanup
                if (StringUtils.equals(command.getCommand(), "sync")) {
                    return setCommandStatusAndReturnExecutionLog(command, JobStatus.error, true, basePath);

                } else {
                    return setCommandStatusAndReturnExecutionLog(command, JobStatus.success, true, basePath);
                }
            }).when(terraformManager).execute(any(), any(), any());

            /*
              We want all 4 commands to be executed.
              Although, all 4 commands are executed, the sync command will return a null path.
              As the `ignoreResult` is set to true, we will continue execution
              however only 3 log files will be streamed + cleaned
             */
            executeCommandsAndVerify(4, 3);
            verify(commandManager, times(1)).deleteExecutionLogFiles(executionLogFileCaptor.capture());
            Assertions.assertThat(executionLogFileCaptor.getValue())
                    .containsExactlyInAnyOrder(
                            basePath.resolve("apply"),
                            basePath.resolve("write_HCL"),
                            basePath.resolve("write_HCL"));
        }


        @Test
        void testExecutionLogCleanupWhenExitEarlyOnFailedCommand(@TempDir Path basePath) {
            //exit early in case of failure
            message.getCommandBundles().get(0).setExitOnFailure(true);

            doAnswer((Answer<Path>) invocation -> {
                Command command = (Command) invocation.getArgument(1);
                // intentionally making the sync command return null path to test graceful cleanup
                if (StringUtils.equals(command.getCommand(), "sync")) {
                    return setCommandStatusAndReturnExecutionLog(command, JobStatus.error, false, basePath);
                } else {
                    return setCommandStatusAndReturnExecutionLog(command, JobStatus.success, true, basePath);
                }
            }).when(terraformManager).execute(any(), any(), any());

            /*
              Commands are executed in sequence
               1. write_HCL
               2. write_HCL
               3. sync
               4. apply

               We are failing fast when sync command fails,
               so we expect 3 commands to be executed and 2 log files to be streamed + cleaned
             */
            executeCommandsAndVerify(3, 2);
            verify(commandManager, times(1)).deleteExecutionLogFiles(executionLogFileCaptor.capture());
            Assertions.assertThat(executionLogFileCaptor.getValue())
                    .containsExactlyInAnyOrder(
                            basePath.resolve("write_HCL"),
                            basePath.resolve("write_HCL")
                    );
        }

        @Test
        void testExecutionLogCleanupWhenLogStreamingToEpFails(@TempDir Path basePath) {
            doAnswer((Answer<Path>) invocation -> {
                Command command = invocation.getArgument(1);
                return setCommandStatusAndReturnExecutionLog(command, JobStatus.success, true, basePath);
            }).when(terraformManager).execute(any(), any(), any());

            doThrow(new IllegalArgumentException("fake")).when(commandLogStreamingProcessor).streamLogsToEP(any(), any(), any());
            commandManager.execute(message);
            // Wait for the command thread to complete
            await().atMost(5, TimeUnit.SECONDS).until(() -> CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1));


            verify(commandLogStreamingProcessor, times(4)).streamLogsToEP(any(), any(), any());

            //we still expect cleanup to occur even though log streaming to ep fails
            verify(commandManager, times(1)).deleteExecutionLogFiles(executionLogFileCaptor.capture());
            Assertions.assertThat(executionLogFileCaptor.getValue())
                    .containsExactlyInAnyOrder(
                            basePath.resolve("apply"),
                            basePath.resolve("write_HCL"),
                            basePath.resolve("write_HCL"),
                            basePath.resolve("sync"));
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
            commandManager.deleteExecutionLogFiles(
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
            commandManager.deleteExecutionLogFiles(
                    List.of(commandLog1, commandLog2, commandLog3, commandLog4)
            );

            Assertions.assertThat(
                    allLogs.stream().noneMatch(path -> Files.exists(path, LinkOption.NOFOLLOW_LINKS))
            ).isTrue();

        }

        private void executeCommandsAndVerify(int expectedNumberOfCommandExecutions,
                                              int expectedNumberOfLogFilesStreamed) {
            commandManager.execute(message);

            // Wait for the command thread to complete
            await().atMost(5, TimeUnit.SECONDS).until(() -> CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1));

            verify(terraformManager, times(expectedNumberOfCommandExecutions)).execute(any(), any(), envArgCaptor.capture());
            verify(commandPublisher, times(1)).sendCommandResponse(responseCaptor.capture(), topicArgCaptor.capture());
            verify(commandLogStreamingProcessor, times(expectedNumberOfLogFilesStreamed)).streamLogsToEP(any(), any(), any());
        }

        private Path setCommandStatusAndReturnExecutionLog(Command targetCommand,
                                                           JobStatus targetStatus,
                                                           boolean ignoreResult,
                                                           Path basePath) {

            if (targetStatus == JobStatus.success) {
                targetCommand.setResult(CommandResult.builder()
                        .status(JobStatus.success)
                        .result(Map.of()).build());
                return basePath.resolve(targetCommand.getCommand());
            } else {
                //simulating a failed command
                targetCommand.setResult(null);
                targetCommand.setIgnoreResult(ignoreResult);
                return null;
            }

        }
    }


}

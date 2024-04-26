package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

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
class CommandManagerTests {

    @SpyBean
    private CommandManager commandManager;

    @Autowired
    private CommandMapper commandMapper;

    @Autowired
    private TerraformManager terraformManager;

    @Autowired
    private CommandPublisher commandPublisher;

    @Autowired
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
        reset(commandPublisher);
        reset(commandManager);
        reset(messagingServiceDelegateService);
        reset(commandLogStreamingProcessor);
    }

    @Test
    void testMultiThreadedCommandManager() throws InterruptedException {

        // Set up the thread pool
        int commandThreadPoolQueueSize = eventPortalProperties.getCommandThreadPoolQueueSize();
        testThreadPool.setCorePoolSize(commandThreadPoolQueueSize);
        testThreadPool.initialize();


        // Build enough requests to fill the command thread pool queue
        List<CommandMessage> messageList = new ArrayList<>();
        for (int i = 0; i < commandThreadPoolQueueSize; i++) {
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

        // Execute all the commands in parallel to fill the command thread pool queue
        IntStream.rangeClosed(1, commandThreadPoolQueueSize).parallel().forEach(i ->
                CompletableFuture.runAsync(() -> commandManager.execute(messageList.get(i - 1)), testThreadPool));

        // Wait for all the threads to complete (add a timeout just in case)
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(commandThreadPoolQueueSize));

        // Verify terraform manager is called
        ArgumentCaptor<Map<String, String>> envArgCaptor = ArgumentCaptor.forClass(Map.class);
        verify(terraformManager, times(commandThreadPoolQueueSize)).execute(any(), any(), envArgCaptor.capture());

        // Verify the env vars are set with the terraform manager is called
        Map<String, String> envVars = envArgCaptor.getValue();
        assert envVars.get("TF_VAR_password").equals("myPassword");
        assert envVars.get("TF_VAR_username").equals("myUsername");
        assert envVars.get("TF_VAR_url").equals("myConnectionUrl");

        ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(commandThreadPoolQueueSize)).sendCommandResponse(messageArgCaptor.capture(), topicArgCaptor.capture());

        Map<String, String> topicVars = topicArgCaptor.getValue();
        assert topicVars.get("orgId").equals(eventPortalProperties.getOrganizationId());
        assert topicVars.get("runtimeAgentId").equals(eventPortalProperties.getRuntimeAgentId());

        // Make sure we get all 10 correlation ids in the response messages
        List<String> receivedCorrelationIds = messageArgCaptor.getAllValues().stream().map(CommandMessage::getCommandCorrelationId).toList();
        List<String> expectedCorrelationIds = IntStream.range(0, commandThreadPoolQueueSize).mapToObj(i -> "myCorrelationId" + i).toList();
        assertTrue(receivedCorrelationIds.size() == expectedCorrelationIds.size() &&
                receivedCorrelationIds.containsAll(expectedCorrelationIds) && expectedCorrelationIds.containsAll(receivedCorrelationIds));
    }

    @Test
    void failSendingResponseBackToEp() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");

        doReturn(Path.of("/some/path/on/disk")).when(terraformManager).execute(any(), any(), any());
        doThrow(new RuntimeException("Error sending response back to EP")).when(commandPublisher).sendCommandResponse(any(), any());
        commandManager.execute(message);

        // Wait for the command thread to complete
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(2));

        ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(2)).sendCommandResponse(messageArgCaptor.capture(), any());

        // Check that we attempted to set Error in the response message
        messageArgCaptor.getAllValues().forEach(commandMessage -> {
            assert commandMessage.getCommandCorrelationId().equals(message.getCommandCorrelationId());
            assert commandMessage.getCommandBundles().get(0).getCommands().get(0).getResult().getStatus().equals(JobStatus.error);
        });

        // Overall status of the request is set to error
        assertEquals(JobStatus.error, messageArgCaptor.getAllValues().get(1).getStatus());
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
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));

        ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(1)).sendCommandResponse(messageArgCaptor.capture(), any());

        assertEquals(JobStatus.error, messageArgCaptor.getValue().getStatus());
    }

    @Test
    void failConfigPushCommand() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");

        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        doThrow(new RuntimeException("Error running command.")).when(commandManager).configPush(commandMapper.map(message));

        commandManager.execute(message);
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));

        ArgumentCaptor<CommandMessage> messageArgCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(1)).sendCommandResponse(messageArgCaptor.capture(), any());

        assertEquals(JobStatus.error, messageArgCaptor.getValue().getStatus());
    }

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

        // Wait for the command thread to complete
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));

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


    @Test
    void testLogStreamingToEP() {
        // Create a command request message
        CommandMessage message = buildCommandMessageForConfigPush();
        doAnswer((Answer<Path>) invocation -> {
            Command command = (Command) invocation.getArgument(1);
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .result(Map.of()).build());
            return Path.of("/some/path/on/disk/for/command-" + command.getCommand());
        }).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<List<Path>> executionLogFileCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Map<String, String>> topicArgCaptor = ArgumentCaptor.forClass(Map.class);
        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        when(messagingServiceDelegateService.getMessagingServiceClient(any())).thenReturn(
                new SolaceHttpSemp(SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl("myConnectionUrl")
                        .build()));

        commandManager.execute(message);

        // Wait for the command thread to complete
        await().atMost(5, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));

        // Verify terraform manager is called
        ArgumentCaptor<Map<String, String>> envArgCaptor = ArgumentCaptor.forClass(Map.class);
        verify(terraformManager, times(4)).execute(any(), any(), envArgCaptor.capture());

        ArgumentCaptor<CommandMessage> responseCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandPublisher, times(1)).sendCommandResponse(responseCaptor.capture(), topicArgCaptor.capture());

        //verify all 4 execution log files are streamed to ep-core
        verify(commandLogStreamingProcessor, times(4)).streamLogsToEP(any(), any(), any());

        //verify all 4 files are then deleted
        verify(commandLogStreamingProcessor, times(1)).deleteExecutionLogFiles(executionLogFileCaptor.capture());
        Assertions.assertThat(executionLogFileCaptor.getValue())
                .containsExactlyInAnyOrder(
                        Path.of("/some/path/on/disk/for/command-apply"),
                        Path.of("/some/path/on/disk/for/command-write_HCL"),
                        Path.of("/some/path/on/disk/for/command-write_HCL"),
                        Path.of("/some/path/on/disk/for/command-sync"));
        assertEquals(JobStatus.success, responseCaptor.getValue().getStatus());
    }

    @Test
    void verifyExitOnFailureIsRespectedWhenTrueAndIgnoreResultIsFalse() {
        CommandMessage message = getCommandMessage("1", 4, true, false);

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
    }

    @Test
    void verifyExitOnFailureIsRespectedWhenTrueAndIgnoreResultIsTrue() {
        CommandMessage message = getCommandMessage("1", 4, true, true);

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
        CommandMessage message = getCommandMessage("1", 4, false, false);

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
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));

        assertTrue(mdcIsSet.get());
    }

    private CommandMessage getCommandMessage(String correlationIdSuffix) {
        return getCommandMessage(correlationIdSuffix, 1, false, false);
    }

    private CommandMessage getCommandMessage(String correlationIdSuffix, int numberOfCommands,
                                             boolean exitOnFailure, boolean ignoreResult) {
        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId(MESSAGING_SERVICE_ID);
        message.setActorId("myActorId");
        message.setOrgId(eventPortalProperties.getOrganizationId());
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId("myCorrelationId" + correlationIdSuffix);
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(exitOnFailure)
                        .commands(IntStream.range(0, numberOfCommands).mapToObj(i -> buildCommand(ignoreResult)).toList())
                        .build()));
        return message;
    }

    private static Command buildCommand(boolean ignoreResult) {
        return Command.builder()
                .commandType(CommandType.terraform)
                .ignoreResult(ignoreResult)
                .body("asdfasdfadsf")
                .command("apply")
                .build();
    }


    private CommandMessage buildCommandMessageForConfigPush() {

        List<Command> commands = List.of(
                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(false)
                        .body("asdfasdfadsf")
                        .command("write_HCL")
                        .build(),
                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(false)
                        .body("asdfasdfadsf")
                        .command("write_HCL")
                        .build(),
                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(true)
                        .body("asdfasdfadsf")
                        .command("sync")
                        .build(),

                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(false)
                        .body("asdfasdfadsf")
                        .command("apply")
                        .build());

        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId(MESSAGING_SERVICE_ID);
        message.setActorId("myActorId");
        message.setOrgId(eventPortalProperties.getOrganizationId());
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId("myCorrelationIdabc");
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(true)
                        .commands(commands)
                        .build()));
        return message;
    }

    private Boolean commandPublisherIsInvoked(int numberOfExpectedInvocations) {
        return Mockito.mockingDetails(commandPublisher).getInvocations().size() == numberOfExpectedInvocations;
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
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));
        return mopMessageCaptor;
    }
}

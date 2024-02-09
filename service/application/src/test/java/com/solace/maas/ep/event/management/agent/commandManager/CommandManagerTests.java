package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;

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
    private TerraformManager terraformManager;

    @Autowired
    private CommandPublisher commandPublisher;

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

        doNothing().when(terraformManager).execute(any(), any(), any());
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
        assertEquals(JobStatus.error, message.getStatus());
    }

    @Test
    void failSettingBrokerSpecificEnvironmentVariables() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");

        doNothing().when(terraformManager).execute(any(), any(), any());
        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        doThrow(new RuntimeException("Could not retrieve or create the messaging service client for [" + MESSAGING_SERVICE_ID + "]."))
                .when(messagingServiceDelegateService).getMessagingServiceClient(MESSAGING_SERVICE_ID);

        commandManager.execute(message);
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));

        assertEquals(JobStatus.error, message.getStatus());
    }

    @Test
    void failConfigPushCommand() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");

        doNothing().when(commandPublisher).sendCommandResponse(any(), any());
        doThrow(new RuntimeException("Error running command.")).when(commandManager).configPush(message);

        commandManager.execute(message);
        await().atMost(10, TimeUnit.SECONDS).until(() -> commandPublisherIsInvoked(1));

        assertEquals(JobStatus.error, message.getStatus());
    }

    @Test
    void testCommandManager() {
        // Create a command request message
        CommandMessage message = getCommandMessage("1");

        doNothing().when(terraformManager).execute(any(), any(), any());

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

        verify(commandPublisher, times(1)).sendCommandResponse(any(), topicArgCaptor.capture());

        Map<String, String> topicVars = topicArgCaptor.getValue();
        assert topicVars.get("orgId").equals(eventPortalProperties.getOrganizationId());
        assert topicVars.get("runtimeAgentId").equals(eventPortalProperties.getRuntimeAgentId());
        assert topicVars.get(COMMAND_CORRELATION_ID).equals(message.getCommandCorrelationId());

        assertEquals(JobStatus.success, message.getStatus());
    }

    @Test
    void verifyExitOnFailureIsRespectedWhenTrue() {
        CommandMessage message = getCommandMessage("1", 4);
        message.getCommandBundles().get(0).setExitOnFailure(true);

        doThrow(new RuntimeException("Error executing command")).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<MOPMessage> mopMessageCaptor = executeCommandAndGetResponseMessage(message);

        CommandMessage commandMessage = (CommandMessage) mopMessageCaptor.getValue();

        // Check top level status
        assertEquals(JobStatus.error, commandMessage.getStatus());
        // The first command in the bundle should be marked with error
        assertEquals(JobStatus.error, commandMessage.getCommandBundles().get(0).getCommands().get(0).getResult().getStatus());
        // The rest of the commands should not be executed and have null results
        assertNull(commandMessage.getCommandBundles().get(0).getCommands().get(1).getResult());
        assertNull(commandMessage.getCommandBundles().get(0).getCommands().get(2).getResult());
        assertNull(commandMessage.getCommandBundles().get(0).getCommands().get(3).getResult());
    }

    @Test
    void verifyExitOnFailureIsRespectedWhenFalse() {
        CommandMessage message = getCommandMessage("1", 4);
        message.getCommandBundles().get(0).setExitOnFailure(false);

        doThrow(new RuntimeException("Error executing command")).when(terraformManager).execute(any(), any(), any());

        ArgumentCaptor<MOPMessage> mopMessageCaptor = executeCommandAndGetResponseMessage(message);

        CommandMessage commandMessage = (CommandMessage) mopMessageCaptor.getValue();

        // Check top level status
        assertEquals(JobStatus.error, commandMessage.getStatus());
        // The first command in the bundle should be marked with error
        assertEquals(JobStatus.error, commandMessage.getCommandBundles().get(0).getCommands().get(0).getResult().getStatus());
        assertEquals(JobStatus.error, commandMessage.getCommandBundles().get(0).getCommands().get(1).getResult().getStatus());
        assertEquals(JobStatus.error, commandMessage.getCommandBundles().get(0).getCommands().get(2).getResult().getStatus());
        assertEquals(JobStatus.error, commandMessage.getCommandBundles().get(0).getCommands().get(3).getResult().getStatus());
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
        return getCommandMessage(correlationIdSuffix, 1);
    }

    private CommandMessage getCommandMessage(String correlationIdSuffix, int numberOfCommands) {
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
                        .exitOnFailure(false)
                        .commands(IntStream.range(0, numberOfCommands).mapToObj(i -> buildCommand()).toList())
                        .build()));
        return message;
    }

    private static Command buildCommand() {
        return Command.builder()
                .commandType(CommandType.terraform)
                .body("asdfasdfadsf")
                .command("apply")
                .build();
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

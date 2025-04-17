package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@Slf4j
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "eventPortal.gateway.messaging.standalone=false",
        "eventPortal.managed=false",
        "eventPortal.incomingRequestQueueName=ep_core_ema_requests_123456_123123",
        "event-portal.gateway.messaging.rto-session=false"
})
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
//CPD-OFF
class CloudManagedEMACommandManagerTests {

    @MockitoSpyBean
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
    void orgIdFromMessageIsUsedThroughout(@TempDir Path basePath) {
        // We're not going to need this in the flow, so to ensure it's not used, we'll set it to null
        eventPortalProperties.setOrganizationId(null);
        doAnswer((Answer<Path>) invocation -> {
            Command command = invocation.getArgument(1);
            return CommandManagerTestHelper.setCommandStatusAndReturnExecutionLog(command, JobStatus.success, true, basePath);
        }).when(terraformManager).execute(any(), any(), any());

        commandManager.execute(message);

        // Wait for the command thread to complete
        await().atMost(5, TimeUnit.SECONDS).until(() -> CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1));

        verify(commandPublisher, times(1)).sendCommandResponse(responseCaptor.capture(), topicArgCaptor.capture());
        Map<String, String> topicVariables = topicArgCaptor.getValue();
        CommandMessage mopMessageResponse = responseCaptor.getValue();
        // With this, we know the input mop message has orgId set, and it's used until the end of the flow.
        assertThat(mopMessageResponse.getOrgId()).isEqualTo("myOrg123");
        assertThat(topicVariables).containsEntry("orgId", mopMessageResponse.getOrgId());
    }

    @Test
    void noLogsStreamingToEP(@TempDir Path basePath) {
        doAnswer((Answer<Path>) invocation -> {
            Command command = (Command) invocation.getArgument(1);
            return CommandManagerTestHelper.setCommandStatusAndReturnExecutionLog(command, JobStatus.success, true, basePath);
        }).when(terraformManager).execute(any(), any(), any());

        commandManager.execute(message);

        // Wait for the command thread to complete
        await().atMost(5, TimeUnit.SECONDS).until(() -> CommandManagerTestHelper.verifyCommandPublisherIsInvoked(commandPublisher, 1));

        verify(terraformManager, times(4)).execute(any(), any(), envArgCaptor.capture());
        verify(commandPublisher, times(1)).sendCommandResponse(responseCaptor.capture(), topicArgCaptor.capture());

        //Logs will not be streamed as EMA is cloud managed
        verify(commandManager, times(0)).streamCommandExecutionLogToEpCore(any(), any(), any());

        //Logs will be cleaned up anyway
        verify(commandManager, times(1)).deleteExecutionLogFiles(executionLogFileCaptor.capture());
        assertThat(executionLogFileCaptor.getValue())
                .containsExactlyInAnyOrder(
                        basePath.resolve("apply"),
                        basePath.resolve("write_HCL"),
                        basePath.resolve("write_HCL"),
                        basePath.resolve("sync"));
    }
}
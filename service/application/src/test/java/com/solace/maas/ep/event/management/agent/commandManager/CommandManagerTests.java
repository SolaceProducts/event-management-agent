package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static com.solace.maas.ep.event.management.agent.constants.Command.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class CommandManagerTests {

    @Autowired
    CommandManager commandManager;

    @Autowired
    TerraformManager terraformManager;

    @Autowired
    CommandPublisher commandPublisher;

    @Autowired
    MessagingServiceDelegateService messagingServiceDelegateService;

    @Autowired
    EventPortalProperties eventPortalProperties;

    @Test
    public void testCommandManager() {
        // Create a command request message
        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setActorId("myActorId");
        message.setOrgId(eventPortalProperties.getOrganizationId());
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId("myCorrelationId");
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(false)
                        .commands(List.of(
                                Command.builder()
                                        .commandType(CommandType.terraform)
                                        .body("asdfasdfadsf")
                                        .command("apply")
                                        .build()))
                        .build()));

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
    }
}

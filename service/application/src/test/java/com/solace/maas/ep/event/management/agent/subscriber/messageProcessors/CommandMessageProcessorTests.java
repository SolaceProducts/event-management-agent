package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@ActiveProfiles("TEST")
class CommandMessageProcessorTests {

    @MockitoBean
    private CommandManager commandManager;
    @MockitoSpyBean
    private CommandMessageProcessor commandMessageProcessor;

    @MockitoBean
    private DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper;

    @Test
    void processMessageWithoutResourceConfig() {

        CommandMessage messageWithoutResources = buildCommandMessage(List.of());
        commandMessageProcessor.processMessage(messageWithoutResources);
        verifyNoInteractions(dynamicResourceConfigurationHelper);
        verify(commandManager, times(1)).execute(any());

    }

    @Test
    void processMessageWithResourceConfig() {
        CommandMessage messageWithoutResources = buildCommandMessage(List.of(
                EventBrokerResourceConfigTestHelper.buildResourceConfiguration(ResourceConfigurationType.SOLACE))
        );
        commandMessageProcessor.processMessage(messageWithoutResources);
        verify(dynamicResourceConfigurationHelper, times(1)).loadSolaceBrokerResourceConfigurations(any());
        verify(commandManager, times(1)).execute(any());
    }

    private CommandMessage buildCommandMessage(List<EventBrokerResourceConfiguration> resources) {
        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId("someId");
        message.setActorId("myActorId");
        message.setResources(resources);
        message.setOrgId("someOrgId");
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId("someId");
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(true)
                        .commands(List.of())
                        .build()));

       return message;
    }
}

package com.solace.maas.ep.event.management.agent.configurationTaskManager;

import com.solace.maas.ep.common.model.ConfigDestination;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.configurationTaskManager.model.ConfigurationTaskBO;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfilePublishTopicException;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskState;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.service.ConfigurationTaskService;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ConfigurationTaskManagerTest {
    public static final String MESSAGING_SERVICE_ID = "id";
    public static final String MESSAGING_SERVICE_TYPE = "TEST_SERVICE";
    @Mock
    EventPortalProperties eventPortalProperties;

    @Mock
    MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @Mock
    ConfigurationTaskService configurationTaskService;

    @InjectMocks
    ConfigurationTaskManager configurationTaskManager;

    @Test
    @SneakyThrows
    public void testConfigurationTaskManagerSuccess(){

        TaskConfig<MsgVpnAclProfilePublishTopicException> aclProfileExceptionConfig = TaskConfig.<MsgVpnAclProfilePublishTopicException>builder()
                .objectType(MsgVpnAclProfile.class.getSimpleName())
                .state(TaskState.PRESENT)
                .configObject(new MsgVpnAclProfilePublishTopicException())
                .build();
        TaskConfig<MsgVpnAclProfile> aclProfileConfig = TaskConfig.<MsgVpnAclProfile>builder()
                .objectType(MsgVpnAclProfile.class.getSimpleName())
                .state(TaskState.PRESENT)
                .configObject(new MsgVpnAclProfile())
                .build();
        ConfigurationTaskBO bo = ConfigurationTaskBO.builder()
                .id("myId")
                .messagingServiceId(MESSAGING_SERVICE_ID)
                .destinations(List.of(ConfigDestination.BROKER.name()))
                .configType("solace_config")
                .taskConfigs(List.of(aclProfileConfig, aclProfileExceptionConfig))
                .messagingServiceType(MESSAGING_SERVICE_TYPE)
                .build();
        boolean result = configurationTaskManager.execute(bo);
        assertThatNoException();
        assertThat(result);
    }

    @BeforeEach
    public void setupMocks(){
        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id(MESSAGING_SERVICE_ID)
                .name("name")
                .type(MESSAGING_SERVICE_TYPE)
                .connections(List.of())
                .build();

        when(messagingServiceDelegateService.getMessagingServiceById("id"))
                .thenReturn(messagingServiceEntity);
        MessagingServiceEntity mockMessagingService = mock(MessagingServiceEntity.class);
        when(configurationTaskService.execute(
                eq(List.of()),
                eq("groupId"),
                eq("myId"),
                any()
        )).thenReturn(true);

    }
}

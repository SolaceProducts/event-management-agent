package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.SolaceTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskState;
import lombok.SneakyThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SolaceTestConfig.class)
@Testcontainers
@SuppressWarnings("PMD")
class AclProfileTaskProcessorTest  {
    public static final String MSG_VPN_NAME = "default";
    public static final String SEMP_USER = "admin";
    public static final String SEMP_PASSWORD = "admin";

    public static final String MSG_SVC_ID = "testService";
    @Container
    public static final GenericContainer solace= new GenericContainer("solace/solace-pubsub-standard")
            .withEnv("username_admin_globalaccesslevel", SEMP_USER)
            .withEnv("username_admin_password", SEMP_PASSWORD)
            .withSharedMemorySize(2L * 1024L * 1024L * 1024L)
            .withReuse(true)
            .withExposedPorts(8080);
    @BeforeEach
    public void setup() throws Throwable{
        final String sempHost = String.format("http://localhost:%s/SEMP/v2/config", solace.getMappedPort(8080));
        SempApiClient sempApiClient = SempApiClient.builder().apiClient(new ApiClient()).baseurl(sempHost).msgVpn(MSG_VPN_NAME).password(SEMP_PASSWORD).username(SEMP_USER).build();
        SolaceSempApiClient sempClient = new SolaceSempApiClient(sempApiClient);
        when(messagingServiceDelegateService.getMessagingServiceClient(MSG_SVC_ID))
                .thenReturn(sempClient);
        this.setupTest();
    }

    @Mock
    public MessagingServiceDelegateService messagingServiceDelegateService;

    @InjectMocks
    private AclProfileTaskProcessor aclProfileConfigurationProcessor;

    @SneakyThrows
    @Test
    public void testHandleMsgVpnAclProfileEventPresentSuccess() {

        MsgVpnAclProfile p = new MsgVpnAclProfile();
        p.setAclProfileName("myAcl");
        p.setMsgVpnName(MSG_VPN_NAME);
        p.setClientConnectDefaultAction(MsgVpnAclProfile.ClientConnectDefaultActionEnum.ALLOW);
        p.setPublishTopicDefaultAction(MsgVpnAclProfile.PublishTopicDefaultActionEnum.DISALLOW);
        p.setSubscribeTopicDefaultAction(MsgVpnAclProfile.SubscribeTopicDefaultActionEnum.DISALLOW);

        TaskConfig<MsgVpnAclProfile> config =
                TaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleMsgVpnAclProfileEventAbsentSuccess() {

        MsgVpnAclProfile p = new MsgVpnAclProfile();
        p.setAclProfileName("myAcl");
        p.setMsgVpnName(MSG_VPN_NAME);
        p.setClientConnectDefaultAction(MsgVpnAclProfile.ClientConnectDefaultActionEnum.ALLOW);
        p.setPublishTopicDefaultAction(MsgVpnAclProfile.PublishTopicDefaultActionEnum.DISALLOW);
        p.setSubscribeTopicDefaultAction(MsgVpnAclProfile.SubscribeTopicDefaultActionEnum.DISALLOW);

        TaskConfig<MsgVpnAclProfile> config =
                TaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        config.setState(TaskState.ABSENT);
        TaskResult r2 = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
        TaskResult r3 = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r3, notNullValue());
        assertThat(r3.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleMsgVpnAclProfileEventAbsentNoOp() {

        MsgVpnAclProfile p = new MsgVpnAclProfile();
        p.setAclProfileName("myAcl");
        p.setMsgVpnName(MSG_VPN_NAME);
        p.setClientConnectDefaultAction(MsgVpnAclProfile.ClientConnectDefaultActionEnum.ALLOW);
        p.setPublishTopicDefaultAction(MsgVpnAclProfile.PublishTopicDefaultActionEnum.DISALLOW);
        p.setSubscribeTopicDefaultAction(MsgVpnAclProfile.SubscribeTopicDefaultActionEnum.DISALLOW);

        TaskConfig<MsgVpnAclProfile> config =
                TaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.ABSENT).build();
        TaskResult r2 = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }
    @SneakyThrows
    @Test
    public void testHandleMsgVpnAclProfileEventFailure() {

        MsgVpnAclProfile p = new MsgVpnAclProfile();
        p.setAclProfileName("myAcl");
        p.setMsgVpnName("not_here_not_now");
        p.setClientConnectDefaultAction(MsgVpnAclProfile.ClientConnectDefaultActionEnum.ALLOW);
        p.setPublishTopicDefaultAction(MsgVpnAclProfile.PublishTopicDefaultActionEnum.DISALLOW);

        TaskConfig<MsgVpnAclProfile> config =
                TaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(false));
        assertThat(r.getLog().getInfo().toString(), containsString("400 Bad"));
    }

    //@Override
    public void setupTest() {

    }
}
package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientUsername;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnTaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskResult;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskState;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@SuppressWarnings("PMD")
class ClientUsernameTaskProcessorTest extends BaseTaskProcessorTest {

    public static final String ACL_PROFILE_NAME = "myAcl";
    @InjectMocks
    private AclProfileTaskProcessor aclProfileConfigurationProcessor;

    @InjectMocks
    private ClientUsernameTaskProcessor clientUsernameTaskProcessor;



    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileClientConnectExceptionEventPresent() {
        MsgVpnClientUsername e = createClientUsername();
        SEMPv2MsgVpnTaskConfig<MsgVpnClientUsername> config = SEMPv2MsgVpnTaskConfig.<MsgVpnClientUsername>builder().configObject(e).objectType(MsgVpnClientUsername.class.getSimpleName()).state(TaskState.PRESENT).build();
        TaskResult r = this.clientUsernameTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.clientUsernameTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @NonNull
    private MsgVpnClientUsername createClientUsername() {
        MsgVpnClientUsername e = new MsgVpnClientUsername();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setEnabled(true);
        e.setClientUsername("new_user");
        e.setClientProfileName("default");
        e.setPassword("default");
        return e;
    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileClientConnectExceptionAbsent() {
        MsgVpnClientUsername e = this.createClientUsername();
        SEMPv2MsgVpnTaskConfig<MsgVpnClientUsername> config = SEMPv2MsgVpnTaskConfig.<MsgVpnClientUsername>builder().configObject(e).objectType(MsgVpnClientUsername.class.getSimpleName()).state(TaskState.PRESENT).build();
        TaskResult o = this.clientUsernameTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(o, notNullValue());
        assertThat(o.isSuccess(), equalTo(true));
        config.setTaskState(TaskState.ABSENT);
        TaskResult r = this.clientUsernameTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.clientUsernameTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileClientConnectExceptionError() {
        MsgVpnClientUsername e = this.createClientUsername();
        e.setAclProfileName("mooAcl");
        SEMPv2MsgVpnTaskConfig<MsgVpnClientUsername> config = SEMPv2MsgVpnTaskConfig.<MsgVpnClientUsername>builder().configObject(e).objectType(MsgVpnClientUsername.class.getSimpleName()).state(TaskState.PRESENT).build();
        TaskResult r = this.clientUsernameTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(false));

    }


    @Override
    public void setupTest() throws Throwable {
        MsgVpnAclProfile p = new MsgVpnAclProfile();
        p.setAclProfileName(ACL_PROFILE_NAME);
        p.setMsgVpnName(MSG_VPN_NAME);
        p.setClientConnectDefaultAction(MsgVpnAclProfile.ClientConnectDefaultActionEnum.ALLOW);
        p.setPublishTopicDefaultAction(MsgVpnAclProfile.PublishTopicDefaultActionEnum.DISALLOW);
        p.setSubscribeTopicDefaultAction(MsgVpnAclProfile.SubscribeTopicDefaultActionEnum.DISALLOW);
        p.setSubscribeShareNameDefaultAction(MsgVpnAclProfile.SubscribeShareNameDefaultActionEnum.ALLOW);

        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfile> config =
                SEMPv2MsgVpnTaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), config);
        if (!r.isSuccess()) {
            throw new RuntimeException("could not bootstrap unit test - can;t create prerequisite ACL Profile");
        }
    }

    @AfterEach
    public void tearDownTest() throws Throwable {
        MsgVpnClientUsername e = this.createClientUsername();
        SEMPv2MsgVpnTaskConfig<MsgVpnClientUsername> configClientUsername = SEMPv2MsgVpnTaskConfig.<MsgVpnClientUsername>builder().configObject(e).objectType(MsgVpnClientUsername.class.getSimpleName()).state(TaskState.ABSENT).build();
        this.clientUsernameTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), configClientUsername);


        MsgVpnAclProfile p = new MsgVpnAclProfile();
        p.setAclProfileName(ACL_PROFILE_NAME);
        p.setMsgVpnName(MSG_VPN_NAME);

        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfile> config =
                SEMPv2MsgVpnTaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.ABSENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), config);
        if (!r.isSuccess()) {
            throw new RuntimeException("could not tear down unit test - unable to delete ACL profile");
        }
    }
}
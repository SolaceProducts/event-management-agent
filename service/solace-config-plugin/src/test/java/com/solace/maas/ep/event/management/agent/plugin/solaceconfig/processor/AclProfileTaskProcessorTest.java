package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnTaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskResult;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskState;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("PMD")
class AclProfileTaskProcessorTest extends BaseTaskProcessorTest {


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

        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfile> config =
                SEMPv2MsgVpnTaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
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

        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfile> config =
                SEMPv2MsgVpnTaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        config.setTaskState(TaskState.ABSENT);
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

        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfile> config =
                SEMPv2MsgVpnTaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.ABSENT).build();
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

        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfile> config =
                SEMPv2MsgVpnTaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(false));
        assertThat(r.getLog().getInfo().toString(), containsString("400 Bad"));
    }

    @Override
    public void setupTest() {

    }
}
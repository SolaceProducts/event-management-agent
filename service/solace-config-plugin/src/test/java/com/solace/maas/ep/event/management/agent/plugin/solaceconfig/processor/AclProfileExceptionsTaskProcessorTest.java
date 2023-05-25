package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.SolaceTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfilePublishTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileSubscribeTopicException;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskState;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SolaceTestConfig.class)
@Testcontainers
@SuppressWarnings("PMD")
class AclProfileExceptionsTaskProcessorTest extends BaseTaskProcessorTest {

    public static final String ACL_PROFILE_NAME = "myAcl";
    @InjectMocks
    private AclProfileTaskProcessor aclProfileConfigurationProcessor;

    @InjectMocks
    private AclProfileClientConnectExceptionTaskProcessor aclProfileClientConnectExceptionTaskProcessor;

    @InjectMocks
    private ACLProfileSubscribeExceptionTaskProcessor aclSubscribeExceptionTaskProcessor;

    @InjectMocks
    private ACLProfilePublishExceptionTaskProcessor aclProfilePublishExceptionTaskProcessor;

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileClientConnectExceptionEventPresent() {
        MsgVpnAclProfileClientConnectException e = new MsgVpnAclProfileClientConnectException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setClientConnectExceptionAddress("10.0.0.0/24");
        TaskConfig<MsgVpnAclProfileClientConnectException> config = TaskConfig.<MsgVpnAclProfileClientConnectException>builder().configObject(e).objectType(MsgVpnAclProfileClientConnectException.class.getSimpleName()).state(TaskState.PRESENT).build();
        TaskResult r = this.aclProfileClientConnectExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.aclProfileClientConnectExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileClientConnectExceptionAbsent() {
        MsgVpnAclProfileClientConnectException e = new MsgVpnAclProfileClientConnectException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setClientConnectExceptionAddress("10.0.0.0/24");
        TaskConfig<MsgVpnAclProfileClientConnectException> config = TaskConfig.<MsgVpnAclProfileClientConnectException>builder().configObject(e).objectType(MsgVpnAclProfileClientConnectException.class.getSimpleName()).state(TaskState.PRESENT).build();
        TaskResult o = this.aclProfileClientConnectExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(o, notNullValue());
        assertThat(o.isSuccess(), equalTo(true));
        config.setState(TaskState.ABSENT);
        TaskResult r = this.aclProfileClientConnectExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.aclProfileClientConnectExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileClientConnectExceptionError() {
        MsgVpnAclProfileClientConnectException e = new MsgVpnAclProfileClientConnectException();
        e.setAclProfileName("mooAcl");
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setClientConnectExceptionAddress("10.0.0.0/24");
        TaskConfig<MsgVpnAclProfileClientConnectException> config = TaskConfig.<MsgVpnAclProfileClientConnectException>builder().configObject(e).objectType(MsgVpnAclProfileClientConnectException.class.getSimpleName()).state(TaskState.PRESENT).build();
        TaskResult r = this.aclProfileClientConnectExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(false));

    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileSubscribeTopicExceptionPresent() {
        MsgVpnAclProfileSubscribeTopicException e = new MsgVpnAclProfileSubscribeTopicException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setSubscribeTopicException("hello/world");
        e.setSubscribeTopicExceptionSyntax(MsgVpnAclProfileSubscribeTopicException.SubscribeTopicExceptionSyntaxEnum.SMF);
        TaskConfig<MsgVpnAclProfileSubscribeTopicException> config =
                TaskConfig.<MsgVpnAclProfileSubscribeTopicException>builder()
                        .configObject(e).objectType(MsgVpnAclProfileSubscribeTopicException.class.getSimpleName())
                        .state(TaskState.PRESENT).build();
        TaskResult r = this.aclSubscribeExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.aclSubscribeExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfileSubscribeTopicExceptionAbsent() {
        MsgVpnAclProfileSubscribeTopicException e = new MsgVpnAclProfileSubscribeTopicException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setSubscribeTopicException("hello/world");
        e.setSubscribeTopicExceptionSyntax(MsgVpnAclProfileSubscribeTopicException.SubscribeTopicExceptionSyntaxEnum.SMF);
        TaskConfig<MsgVpnAclProfileSubscribeTopicException> config =
                TaskConfig.<MsgVpnAclProfileSubscribeTopicException>builder()
                        .configObject(e).objectType(MsgVpnAclProfileSubscribeTopicException.class.getSimpleName())
                        .state(TaskState.PRESENT).build();
        TaskResult o = this.aclSubscribeExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(o, notNullValue());
        assertThat(o.isSuccess(), equalTo(true));
        config.setState(TaskState.ABSENT);
        TaskResult r = this.aclSubscribeExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.aclSubscribeExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleMsgVpnAclProfileSubscribeTopicExceptionError() {
        MsgVpnAclProfileSubscribeTopicException e = new MsgVpnAclProfileSubscribeTopicException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName("mooAcl");
        e.setSubscribeTopicException("hello/world");
        e.setSubscribeTopicExceptionSyntax(MsgVpnAclProfileSubscribeTopicException.SubscribeTopicExceptionSyntaxEnum.SMF);
        TaskConfig<MsgVpnAclProfileSubscribeTopicException> config =
                TaskConfig.<MsgVpnAclProfileSubscribeTopicException>builder()
                        .configObject(e).objectType(MsgVpnAclProfileSubscribeTopicException.class.getSimpleName())
                        .state(TaskState.PRESENT).build();
        TaskResult o = this.aclSubscribeExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(o, notNullValue());
        assertThat(o.isSuccess(), equalTo(false));


    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfilePublishTopicExceptionPresent() {
        MsgVpnAclProfilePublishTopicException e = new MsgVpnAclProfilePublishTopicException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setPublishTopicException("hello/world");
        e.setPublishTopicExceptionSyntax(MsgVpnAclProfilePublishTopicException.PublishTopicExceptionSyntaxEnum.SMF);
        TaskConfig<MsgVpnAclProfilePublishTopicException> config =
                TaskConfig.<MsgVpnAclProfilePublishTopicException>builder()
                        .configObject(e).objectType(MsgVpnAclProfilePublishTopicException.class.getSimpleName())
                        .state(TaskState.PRESENT).build();
        TaskResult r = this.aclProfilePublishExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.aclProfilePublishExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleEventMsgVpnAclProfilePublishTopicExceptionAbsent() {
        MsgVpnAclProfilePublishTopicException e = new MsgVpnAclProfilePublishTopicException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName(MSG_VPN_NAME);
        e.setPublishTopicException("hello/world");
        e.setPublishTopicExceptionSyntax(MsgVpnAclProfilePublishTopicException.PublishTopicExceptionSyntaxEnum.SMF);
        TaskConfig<MsgVpnAclProfilePublishTopicException> config =
                TaskConfig.<MsgVpnAclProfilePublishTopicException>builder()
                        .configObject(e).objectType(MsgVpnAclProfilePublishTopicException.class.getSimpleName())
                        .state(TaskState.PRESENT).build();
        TaskResult o = this.aclProfilePublishExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(o, notNullValue());
        assertThat(o.isSuccess(), equalTo(true));
        config.setState(TaskState.ABSENT);
        TaskResult r = this.aclProfilePublishExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r, notNullValue());
        assertThat(r.isSuccess(), equalTo(true));
        TaskResult r2 = this.aclProfilePublishExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(r2, notNullValue());
        assertThat(r2.isSuccess(), equalTo(true));
    }

    @SneakyThrows
    @Test
    public void testHandleMsgVpnAclProfilePublishTopicExceptionError() {
        MsgVpnAclProfilePublishTopicException e = new MsgVpnAclProfilePublishTopicException();
        e.setAclProfileName(ACL_PROFILE_NAME);
        e.setMsgVpnName("mooAcl");
        e.setPublishTopicException("hello/world");
        e.setPublishTopicExceptionSyntax(MsgVpnAclProfilePublishTopicException.PublishTopicExceptionSyntaxEnum.SMF);
        TaskConfig<MsgVpnAclProfilePublishTopicException> config =
                TaskConfig.<MsgVpnAclProfilePublishTopicException>builder()
                        .configObject(e).objectType(MsgVpnAclProfilePublishTopicException.class.getSimpleName())
                        .state(TaskState.PRESENT).build();
        TaskResult o = this.aclProfilePublishExceptionTaskProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, MSG_SVC_ID), config);
        assertThat(o, notNullValue());
        assertThat(o.isSuccess(), equalTo(false));


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

        TaskConfig<MsgVpnAclProfile> config =
                TaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.PRESENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), config);
        if (!r.isSuccess()) {
            throw new RuntimeException("could not bootstrap unit test - can;t create prerequisite ACL Profile");
        }
    }

    @AfterEach
    public void tearDownTest() throws Throwable {
        MsgVpnAclProfile p = new MsgVpnAclProfile();
        p.setAclProfileName(ACL_PROFILE_NAME);
        p.setMsgVpnName(MSG_VPN_NAME);

        TaskConfig<MsgVpnAclProfile> config =
                TaskConfig.<MsgVpnAclProfile>builder().objectType("MsgVpnAclProfile").configObject(p).state(TaskState.ABSENT).build();
        TaskResult r = aclProfileConfigurationProcessor.handleEvent(Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), config);
        if (!r.isSuccess()) {
            throw new RuntimeException("could not tear down unit test - unable to delete ACL profile");
        }
    }
}
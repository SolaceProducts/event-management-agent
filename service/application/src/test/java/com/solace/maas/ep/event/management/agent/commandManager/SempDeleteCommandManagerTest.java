package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.AclProfileApi;
import com.solace.client.sempv2.api.AuthorizationGroupApi;
import com.solace.client.sempv2.api.ClientUsernameApi;
import com.solace.client.sempv2.api.QueueApi;
import com.solace.client.sempv2.api.RestDeliveryPointApi;
import com.solace.client.sempv2.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.SempDeleteCommandManager;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.solace.maas.ep.common.model.SempEntityType.solaceAclProfile;
import static com.solace.maas.ep.common.model.SempEntityType.solaceAclPublishTopicException;
import static com.solace.maas.ep.common.model.SempEntityType.solaceAclSubscribeTopicException;
import static com.solace.maas.ep.common.model.SempEntityType.solaceAuthorizationGroup;
import static com.solace.maas.ep.common.model.SempEntityType.solaceClientUsername;
import static com.solace.maas.ep.common.model.SempEntityType.solaceQueue;
import static com.solace.maas.ep.common.model.SempEntityType.solaceQueueSubscriptionTopic;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdp;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdpRestConsumer;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants.SEMP_DELETE_DATA;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants.SEMP_DELETE_ENTITY_TYPE;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants.SEMP_DELETE_OPERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class SempDeleteCommandManagerTest {

    private final static String DIR_SEMP_RESOURCES = "src/test/resources/sempResponses/";
    private final static String SEMP_RESPONSE_MISSING_RESOURCE = "sempResponseMissingResource.json";

    @SpyBean
    private SempDeleteCommandManager sempDeleteCommandManager;

    private SempApiProvider sempApiProvider;


    @BeforeEach
    void reset() throws ApiException {
        sempApiProvider = Mockito.mock(SempApiProvider.class);
    }


    // test deleteMsgVpnAclProfile
    @Test
    void testDeleteAclProfileHappyPath() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfile(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclParametersParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfile("default", "aclProfileName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteAclProfileWithValidationException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfile(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclParametersParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(aclProfileApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteAclProfile() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfile(any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclParametersParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfile("default", "aclProfileName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteAclProfileWithException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfile(any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclParametersParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfile("default", "aclProfileName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    // test DeleteAclPublishTopicException
    @Test
    void testDeleteAclPublishTopicExceptionHappyPath() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfilePublishTopicException(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclPublishTopicExceptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfilePublishTopicException("default", "aclProfileName", "smf", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteAclPublishTopicExceptionWithValidationException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfilePublishTopicException(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclPublishTopicExceptionParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(aclProfileApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteAclProfilePublishTopicException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfilePublishTopicException(any(), any(), any(), any()))
                .thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclPublishTopicExceptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfilePublishTopicException("default", "aclProfileName", "smf", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteAclProfilePublishTopicExceptionWithException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfilePublishTopicException(any(), any(), any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclPublishTopicExceptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfilePublishTopicException("default", "aclProfileName", "smf", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    // test DeleteAclSubscribeTopicException
    @Test
    void testDeleteAclProfileSubscribeTopicExceptionHappyPath() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfileSubscribeTopicException(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclSubscribeTopicExceptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfileSubscribeTopicException("default", "aclProfileName", "smf", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteAclProfileSubscribeTopicExceptionWithValidationException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfileSubscribeTopicException(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclSubscribeTopicExceptionParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(aclProfileApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteAclProfileSubscribeTopicException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfileSubscribeTopicException(any(), any(), any(), any()))
                .thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclSubscribeTopicExceptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfileSubscribeTopicException("default", "aclProfileName", "smf", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteAclProfileSubscribeTopicExceptionWithException() throws ApiException {
        AclProfileApi aclProfileApi = Mockito.mock(AclProfileApi.class);
        when(sempApiProvider.getAclProfileApi()).thenReturn(aclProfileApi);
        when((aclProfileApi).deleteMsgVpnAclProfileSubscribeTopicException("default", "aclProfileName", "smf", "a/b/c"))
                .thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAclSubscribeTopicExceptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(aclProfileApi).deleteMsgVpnAclProfileSubscribeTopicException("default", "aclProfileName", "smf", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    // test DeleteSolaceClientUsername

    @Test
    void testDeleteClientUsernameHappyPath() throws ApiException {
        ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
        when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
        when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteClientUsernameParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(clientUsernameApi).deleteMsgVpnClientUsername("default", "clientUsername");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteClientUsernameWithValidationException() throws ApiException {
        ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
        when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
        when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteClientUsernameParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(clientUsernameApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testDeleteClientUsernameWithException() throws ApiException {
        ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
        when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
        when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteClientUsernameParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(clientUsernameApi).deleteMsgVpnClientUsername("default", "clientUsername");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteClientUsername() throws ApiException {
        ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
        when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
        when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteClientUsernameParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(clientUsernameApi).deleteMsgVpnClientUsername("default", "clientUsername");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    // test DeleteAuthorizationGroup

    @Test
    void testDeleteAuthorizationGroupHappyPath() throws ApiException {
        AuthorizationGroupApi authorizationGroupApi = Mockito.mock(AuthorizationGroupApi.class);
        when(sempApiProvider.getAuthorizationGroupApi()).thenReturn(authorizationGroupApi);
        when((authorizationGroupApi).deleteMsgVpnAuthorizationGroup(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAuthorizationGroupParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(authorizationGroupApi).deleteMsgVpnAuthorizationGroup("default", "authorizationGroupName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteAuthorizationGroupWithValidationException() throws ApiException {
        AuthorizationGroupApi authorizationGroupApi = Mockito.mock(AuthorizationGroupApi.class);
        when(sempApiProvider.getAuthorizationGroupApi()).thenReturn(authorizationGroupApi);
        when((authorizationGroupApi).deleteMsgVpnAuthorizationGroup(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAuthorizationGroupParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(authorizationGroupApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testDeleteAuthorizationGroupWithException() throws ApiException {
        AuthorizationGroupApi authorizationGroupApi = Mockito.mock(AuthorizationGroupApi.class);
        when(sempApiProvider.getAuthorizationGroupApi()).thenReturn(authorizationGroupApi);
        when((authorizationGroupApi).deleteMsgVpnAuthorizationGroup(any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAuthorizationGroupParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(authorizationGroupApi).deleteMsgVpnAuthorizationGroup("default", "authorizationGroupName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteAuthorizationGroup() throws ApiException {
        AuthorizationGroupApi authorizationGroupApi = Mockito.mock(AuthorizationGroupApi.class);
        when(sempApiProvider.getAuthorizationGroupApi()).thenReturn(authorizationGroupApi);
        when((authorizationGroupApi).deleteMsgVpnAuthorizationGroup(any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteAuthorizationGroupParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(authorizationGroupApi).deleteMsgVpnAuthorizationGroup("default", "authorizationGroupName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }


    // test DeleteSolaceQueue
    @Test
    void testDeleteQueueHappyPath() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueue(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(queueApi).deleteMsgVpnQueue("default", "someQueueName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteQueueWithValidationException() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueue(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(queueApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testDeleteQueueWithException() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueue(any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(queueApi).deleteMsgVpnQueue("default", "someQueueName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteQueue() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueue(any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(queueApi).deleteMsgVpnQueue("default", "someQueueName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    // test DeleteSolaceQueueTopicSubscription
    @Test
    void testDeleteQueueTopicSubscriptionHappyPath() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueueSubscription(any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueSubscriptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(queueApi).deleteMsgVpnQueueSubscription("default", "someQueueName", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteQueueTopicSubscriptionWithException() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueueSubscription(any(), any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueSubscriptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(queueApi).deleteMsgVpnQueueSubscription("default", "someQueueName", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testDeleteQueueTopicSubscriptionWithValidationException() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueueSubscription(any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueSubscriptionParameters(false))
                .build();

        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(queueApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteQueueTopicSubscriptionWithException() throws ApiException {
        QueueApi queueApi = Mockito.mock(QueueApi.class);
        when(sempApiProvider.getQueueApi()).thenReturn(queueApi);
        when((queueApi).deleteMsgVpnQueueSubscription(any(), any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteQueueSubscriptionParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(queueApi).deleteMsgVpnQueueSubscription("default", "someQueueName", "a/b/c");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    // test DeleteRdp
    @Test
    void testDeleteRdpHappyPath() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPoint(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).deleteMsgVpnRestDeliveryPoint("default", "someRdp");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteRdpWithValidationException() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPoint(any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(rdpApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testDeleteRdpWithException() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPoint(any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).deleteMsgVpnRestDeliveryPoint("default", "someRdp");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteRdp() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPoint(any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).deleteMsgVpnRestDeliveryPoint("default", "someRdp");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    // test DeleteRdpRestConsumer
    @Test
    void testDeleteRdpRestConsumerHappyPath() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer(any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpRestConsumerParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer("default", "someRdp", "someRestConsumerName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testDeleteRdpRestConsumerWithValidationException() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer(any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpRestConsumerParameters(false))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(rdpApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testDeleteRdpRestConsumerWithException() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer(any(), any(), any())).thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpRestConsumerParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer("default", "someRdp", "someRestConsumerName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteRdpRestConsumer() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer(any(), any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_DELETE_OPERATION)
                .parameters(createDeleteRdpRestConsumerParameters(true))
                .build();
        sempDeleteCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer("default", "someRdp", "someRestConsumerName");
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    // helpers

    private Map<String, Object> createDeleteQueueSubscriptionParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceQueueSubscriptionTopic.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("queueName", "someQueueName");
        }
        data.put("topicName", "a/b/c");
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteQueueParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceQueue.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("queueName", "someQueueName");
        }
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteRdpParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceRdp.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("rdpName", "someRdp");
        }
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteRdpRestConsumerParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceRdpRestConsumer.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        data.put("rdpName", "someRdp");
        if (valid) {
            data.put("restConsumerName", "someRestConsumerName");
        }
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAclSubscribeTopicExceptionParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceAclSubscribeTopicException.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("aclProfileName", "aclProfileName");
        }
        data.put("subscribeTopic", "a/b/c");
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAclPublishTopicExceptionParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceAclPublishTopicException.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("aclProfileName", "aclProfileName");
        }
        data.put("publishTopic", "a/b/c");
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAclParametersParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceAclProfile.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("aclProfileName", "aclProfileName");
        }
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteClientUsernameParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceClientUsername.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("clientUsername", "clientUsername");
        }
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAuthorizationGroupParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_DELETE_ENTITY_TYPE, solaceAuthorizationGroup.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("authorizationGroupName", "authorizationGroupName");
        }
        parameters.put(SEMP_DELETE_DATA, data);
        return parameters;
    }

    private ApiException createaServerErrorApiException() {
        return new ApiException(500, "", new HashMap<>(), "{\"some\":\"error_message\"}");
    }

    private ApiException createaNotFoundApiException(String resourceName) {
        return new ApiException(400, "", new HashMap<>(), readSempResponseResource(resourceName));
    }

    private String readSempResponseResource(String resourceName) {
        try {
            return Files.readString(Path.of(DIR_SEMP_RESOURCES + resourceName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

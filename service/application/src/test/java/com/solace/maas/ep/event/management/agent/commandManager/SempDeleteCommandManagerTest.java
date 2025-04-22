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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

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
import static com.solace.maas.ep.common.model.SempEntityType.solaceClientCertificateUsername;
import static com.solace.maas.ep.common.model.SempEntityType.solaceClientUsername;
import static com.solace.maas.ep.common.model.SempEntityType.solaceQueue;
import static com.solace.maas.ep.common.model.SempEntityType.solaceQueueSubscriptionTopic;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdp;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdpOauthJwtClaim;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdpQueueBinding;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdpQueueBindingProtectedRequestHeader;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdpQueueBindingRequestHeader;
import static com.solace.maas.ep.common.model.SempEntityType.solaceRdpRestConsumer;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants.SEMP_COMMAND_DATA;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants.SEMP_DELETE_OPERATION;
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

    @MockitoSpyBean
    private SempDeleteCommandManager sempDeleteCommandManager;

    private SempApiProvider sempApiProvider;


    @BeforeEach
    void reset() {
        sempApiProvider = Mockito.mock(SempApiProvider.class);
    }


    //CPD-OFF
    @Nested
    class AclProfile {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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
        void withException() throws ApiException {
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

    }

    @Nested
    class PublishTopicException {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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
        void withException() throws ApiException {
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

    }

    @Nested
    class SubscribeTopicException {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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
        void withException() throws ApiException {
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

    }

    @Nested
    class ClientUsername {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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
    }


    @Nested
    class ClientCertificateUsername {
        @Test
        void happyPath() throws ApiException {
            ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
            when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
            when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteClientCertificateUsernameParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(clientUsernameApi).deleteMsgVpnClientUsername("default", "clientUsername");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }

        @Test
        void withValidationException() throws ApiException {
            ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
            when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
            when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteClientCertificateUsernameParameters(false))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verifyNoInteractions(clientUsernameApi);
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withException() throws ApiException {
            ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
            when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
            when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenThrow(createaServerErrorApiException());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteClientCertificateUsernameParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(clientUsernameApi).deleteMsgVpnClientUsername("default", "clientUsername");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withNotFoundException() throws ApiException {
            ClientUsernameApi clientUsernameApi = Mockito.mock(ClientUsernameApi.class);
            when(sempApiProvider.getClientUsernameApi()).thenReturn(clientUsernameApi);
            when((clientUsernameApi).deleteMsgVpnClientUsername(any(), any())).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteClientCertificateUsernameParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(clientUsernameApi).deleteMsgVpnClientUsername("default", "clientUsername");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }
    }

    @Nested
    class AuthorizationGroup {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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

    }

    @Nested
    class SolaceQueue {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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
    }

    @Nested
    class SolaceQueueTopicSubscription {
        @Test
        void happyPath() throws ApiException {
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
        void withException() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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

    }

    @Nested
    class Rdp {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
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
    }


    @Nested
    class RdpRestConsumer {
        @Test
        void happyPath() throws ApiException {
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
        void withValidationException() throws ApiException {
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
        void withException() throws ApiException {
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
        void withNotFoundException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer(any(), any(), any()))
                    .thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpRestConsumerParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointRestConsumer("default", "someRdp", "someRestConsumerName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }
    }


    @Nested
    class RdpRestConsumerOauthJwtClaim {

        @Test
        void happyPath() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    any(),
                    any(),
                    any(),
                    any())
            ).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpOauthJwtParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    "default",
                    "someRdp",
                    "someRestConsumerName",
                    "someOauthJwtClaimName"
            );
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }

        @Test
        void withValidationException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    any(),
                    any(),
                    any(),
                    any())
            ).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpOauthJwtParameters(false))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verifyNoInteractions(rdpApi);
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    any(),
                    any(),
                    any(),
                    any())
            ).thenThrow(createaServerErrorApiException());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpOauthJwtParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    "default",
                    "someRdp",
                    "someRestConsumerName",
                    "someOauthJwtClaimName"
            );
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withNotFoundException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    any(),
                    any(),
                    any(),
                    any())
            ).thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpOauthJwtParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    "default",
                    "someRdp",
                    "someRestConsumerName",
                    "someOauthJwtClaimName"
            );
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }
    }

    @Nested
    class RdpQueueBinding {
        @Test
        void happyPath() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBinding(any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBinding("default", "someRdp", "someQueueBindingName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }

        @Test
        void withValidationException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBinding(any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingParameters(false))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verifyNoInteractions(rdpApi);
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBinding(any(), any(), any())).thenThrow(createaServerErrorApiException());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBinding("default", "someRdp", "someQueueBindingName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withNotFoundException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBinding(any(), any(), any()))
                    .thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingParameters(true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBinding("default", "someRdp", "someQueueBindingName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }
    }

    @Nested
    class RdpQueueBindingRequestHeader {

        @Test
        void happyPath() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(true, false))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader("default", "someRdp", "someQueueBindingName", "someHeaderName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }

        @Test
        void withValidationException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(false, false))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verifyNoInteractions(rdpApi);
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(any(), any(), any(), any())).thenThrow(createaServerErrorApiException());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(true, false))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader("default", "someRdp", "someQueueBindingName", "someHeaderName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withNotFoundException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(any(), any(), any(), any()))
                    .thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(true, false))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader("default", "someRdp", "someQueueBindingName", "someHeaderName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }
    }

    @Nested
    class RdpQueueBindingProtectedRequestHeader {
        @Test
        void happyPath() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(true, true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader("default", "someRdp", "someQueueBindingName", "someHeaderName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }

        @Test
        void withValidationException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader(any(), any(), any(), any())).thenReturn(new SempMetaOnlyResponse());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(false, true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verifyNoInteractions(rdpApi);
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader(any(), any(), any(), any()))
                    .thenThrow(createaServerErrorApiException());
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(true, true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader("default", "someRdp", "someQueueBindingName", "someHeaderName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        }

        @Test
        void withNotFoundException() throws ApiException {
            RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
            when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
            when((rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader(any(), any(), any(), any()))
                    .thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
            Command cmd = Command.builder()
                    .commandType(CommandType.semp)
                    .command(SEMP_DELETE_OPERATION)
                    .parameters(createDeleteRdpQueueBindingRequestHeaderParameters(true, true))
                    .build();
            sempDeleteCommandManager.execute(cmd, sempApiProvider);
            verify(rdpApi).deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader("default", "someRdp", "someQueueBindingName", "someHeaderName");
            assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
        }
    }

    private Map<String, Object> createDeleteQueueSubscriptionParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceQueueSubscriptionTopic.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("queueName", "someQueueName");
        }
        data.put("topicName", "a/b/c");
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteQueueParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceQueue.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("queueName", "someQueueName");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteRdpParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceRdp.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("rdpName", "someRdp");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteRdpRestConsumerParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceRdpRestConsumer.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        data.put("rdpName", "someRdp");
        if (valid) {
            data.put("restConsumerName", "someRestConsumerName");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteRdpOauthJwtParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceRdpOauthJwtClaim.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        data.put("rdpName", "someRdp");
        data.put("restConsumerName", "someRestConsumerName");
        if (valid) {
            data.put("oauthJwtClaimName", "someOauthJwtClaimName");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteRdpQueueBindingParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceRdpQueueBinding.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        data.put("rdpName", "someRdp");
        if (valid) {
            data.put("queueBindingName", "someQueueBindingName");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteRdpQueueBindingRequestHeaderParameters(boolean valid, boolean isProtected) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, isProtected ? solaceRdpQueueBindingProtectedRequestHeader.name() : solaceRdpQueueBindingRequestHeader.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        data.put("rdpName", "someRdp");
        if (valid) {
            data.put("queueBindingName", "someQueueBindingName");
            data.put("headerName", "someHeaderName");
            data.put("isProtected", isProtected ? "true" : "false");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAclSubscribeTopicExceptionParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceAclSubscribeTopicException.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("aclProfileName", "aclProfileName");
        }
        data.put("subscribeTopic", "a/b/c");
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAclPublishTopicExceptionParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceAclPublishTopicException.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("aclProfileName", "aclProfileName");
        }
        data.put("publishTopic", "a/b/c");
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAclParametersParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceAclProfile.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("aclProfileName", "aclProfileName");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteClientUsernameParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceClientUsername.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("clientUsername", "clientUsername");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteClientCertificateUsernameParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceClientCertificateUsername.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("clientUsername", "clientUsername");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
        return parameters;
    }

    private Map<String, Object> createDeleteAuthorizationGroupParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceAuthorizationGroup.name());

        Map<String, String> data = new HashMap<>();
        data.put("msgVpn", "default");
        if (valid) {
            data.put("authorizationGroupName", "authorizationGroupName");
        }
        parameters.put(SEMP_COMMAND_DATA, data);
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
    //CPD-ON
}

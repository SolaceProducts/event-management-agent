package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.AclProfileApi;
import com.solace.client.sempv2.api.AuthorizationGroupApi;
import com.solace.client.sempv2.api.ClientUsernameApi;
import com.solace.client.sempv2.api.QueueApi;
import com.solace.client.sempv2.api.RestDeliveryPointApi;
import com.solace.maas.ep.common.model.SempAclProfileDeletionRequest;
import com.solace.maas.ep.common.model.SempAclPublishTopicExceptionDeletionRequest;
import com.solace.maas.ep.common.model.SempAclSubscribeTopicExceptionDeletionRequest;
import com.solace.maas.ep.common.model.SempAuthorizationGroupDeletionRequest;
import com.solace.maas.ep.common.model.SempClientUsernameDeletionRequest;
import com.solace.maas.ep.common.model.SempEntityType;
import com.solace.maas.ep.common.model.SempQueueDeletionRequest;
import com.solace.maas.ep.common.model.SempQueueTopicSubscriptionDeletionRequest;
import com.solace.maas.ep.common.model.SempRdpDeletionRequest;
import com.solace.maas.ep.common.model.SempRdpOauthJwtClaimDeletionRequest;
import com.solace.maas.ep.common.model.SempRdpQueueBindingDeletionRequest;
import com.solace.maas.ep.common.model.SempRdpQueueBindingRequestHeaderDeletionRequest;
import com.solace.maas.ep.common.model.SempRdpRestConsumerDeletionRequest;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SempDeleteCommandManager extends AbstractSempCommandManager {
    private final ObjectMapper objectMapper;

    public SempDeleteCommandManager(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }


    @Override
    public String supportedSempCommand() {
        return SempCommandConstants.SEMP_DELETE_OPERATION;
    }

    @Override
    protected void executeSempCommand(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        String entityType = (String) command.getParameters().get(SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE);
        SempEntityType deletionEntityType;
        if (entityType == null) {
            throw new IllegalArgumentException("Entity type of a SEMP delete command must not be null");
        }
        try {
            deletionEntityType = SempEntityType.valueOf(entityType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported SEMP delete entity type:" + entityType);
        }
        switch (deletionEntityType) {
            case solaceAclProfile:
                executeDeleteAclProfile(command, sempApiProvider);
                break;
            case solaceQueue:
                executeDeleteSolaceQueue(command, sempApiProvider);
                break;
            case solaceQueueSubscriptionTopic:
                executeDeleteSolaceQueueTopicSubscription(command, sempApiProvider);
                break;
            case solaceClientUsername:
            case solaceClientCertificateUsername:
                executeDeleteClientUsername(command, sempApiProvider);
                break;
            case solaceAuthorizationGroup:
                executeDeleteAuthorizationGroup(command, sempApiProvider);
                break;
            case solaceAclSubscribeTopicException:
                executeDeleteAclSubscribeTopicException(command, sempApiProvider);
                break;
            case solaceRdp:
                executeDeleteRdp(command, sempApiProvider);
                break;
            case solaceRdpRestConsumer:
                executeDeleteRdpRestConsumer(command, sempApiProvider);
                break;
            case solaceRdpQueueBinding:
                executeRdpQueueBinding(command, sempApiProvider);
                break;
            case solaceRdpQueueBindingProtectedRequestHeader:
            case solaceRdpQueueBindingRequestHeader:
                executeRdpQueueBindingRequestHeader(command, sempApiProvider);
                break;
            case solaceAclPublishTopicException:
                executeDeleteAclPublishTopicException(command, sempApiProvider);
                break;
            case solaceRdpOauthJwtClaim:
                executeDeleteRdpOauthJwtClaims(command, sempApiProvider);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported entity type for deletion: " + deletionEntityType);
        }
    }


    private void executeDeleteAclProfile(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AclProfileApi aclProfileApi = sempApiProvider.getAclProfileApi();
        SempAclProfileDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempAclProfileDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getAclProfileName(), "ACL profile name must not be empty");

        log.info("SEMP delete: Deleting ACL profile");
        try {
            aclProfileApi.deleteMsgVpnAclProfile(request.getMsgVpn(), request.getAclProfileName());
        } catch (ApiException e) {
            handleSempOperationException(e, "ACL profile", supportedSempCommand());
        }
    }

    private void executeDeleteAclPublishTopicException(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AclProfileApi aclProfileApi = sempApiProvider.getAclProfileApi();
        SempAclPublishTopicExceptionDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempAclPublishTopicExceptionDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getAclProfileName(), "ACL profile name must not be empty");
        Validate.notEmpty(request.getPublishTopic(), "Publish topic must not be empty");
        log.info("SEMP delete: Deleting ACL publish topic exception");
        try {
            aclProfileApi.deleteMsgVpnAclProfilePublishTopicException(request.getMsgVpn(), request.getAclProfileName(), "smf", request.getPublishTopic());
        } catch (ApiException e) {
            handleSempOperationException(e, "ACL publish topic exception", supportedSempCommand());
        }
    }

    private void executeDeleteAclSubscribeTopicException(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AclProfileApi aclProfileApi = sempApiProvider.getAclProfileApi();
        SempAclSubscribeTopicExceptionDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempAclSubscribeTopicExceptionDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getAclProfileName(), "ACL profile name must not be empty");
        Validate.notEmpty(request.getSubscribeTopic(), "Subscribe topic must not be empty");

        log.info("SEMP delete: Deleting ACL subscribe topic exception");
        try {
            aclProfileApi.deleteMsgVpnAclProfileSubscribeTopicException(request.getMsgVpn(), request.getAclProfileName(), "smf", request.getSubscribeTopic());
        } catch (ApiException e) {
            handleSempOperationException(e, "ACL subscribe topic exception", supportedSempCommand());
        }
    }

    private void executeDeleteAuthorizationGroup(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AuthorizationGroupApi authorizationGroupApi = sempApiProvider.getAuthorizationGroupApi();
        SempAuthorizationGroupDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempAuthorizationGroupDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getAuthorizationGroupName(), "Authorization group name must not be empty");

        log.info("SEMP delete: Deleting authorization group");
        try {
            authorizationGroupApi.deleteMsgVpnAuthorizationGroup(request.getMsgVpn(), request.getAuthorizationGroupName());
        } catch (ApiException e) {
            handleSempOperationException(e, "Authorization group", supportedSempCommand());
        }
    }

    private void executeDeleteClientUsername(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        ClientUsernameApi clientUsernameApi = sempApiProvider.getClientUsernameApi();
        SempClientUsernameDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempClientUsernameDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getClientUsername(), "Client username must not be empty");
        log.info("SEMP delete: Deleting client username");
        try {
            clientUsernameApi.deleteMsgVpnClientUsername(request.getMsgVpn(), request.getClientUsername());
        } catch (ApiException e) {
            handleSempOperationException(e, "Client username", supportedSempCommand());
        }
    }

    private void executeDeleteSolaceQueue(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        QueueApi queueApi = sempApiProvider.getQueueApi();
        SempQueueDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempQueueDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getQueueName(), "Queue name must not be empty");
        log.info("SEMP delete: Deleting queue");
        try {
            queueApi.deleteMsgVpnQueue(request.getMsgVpn(), request.getQueueName());
        } catch (ApiException e) {
            handleSempOperationException(e, "Queue", supportedSempCommand());
        }
    }

    private void executeDeleteSolaceQueueTopicSubscription(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        QueueApi queueApi = sempApiProvider.getQueueApi();
        SempQueueTopicSubscriptionDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempQueueTopicSubscriptionDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getQueueName(), "Queue name must not be empty");
        Validate.notEmpty(request.getTopicName(), "Topic name must not be empty");
        log.info("SEMP delete: Deleting queue subscription");
        try {
            queueApi.deleteMsgVpnQueueSubscription(request.getMsgVpn(), request.getQueueName(), request.getTopicName());
        } catch (ApiException e) {
            handleSempOperationException(e, "Subscription", supportedSempCommand());
        }
    }


    private void executeDeleteRdp(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempRdpDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        log.info("SEMP delete: Deleting Rest Delivery Point");
        try {
            restDeliveryPointApi.deleteMsgVpnRestDeliveryPoint(request.getMsgVpn(), request.getRdpName());
        } catch (ApiException e) {
            handleSempOperationException(e, "Rest Delivery Point", supportedSempCommand());
        }
    }


    private void executeDeleteRdpRestConsumer(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpRestConsumerDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempRdpRestConsumerDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getRestConsumerName(), "Rest Consumer name must not be empty");
        log.info("SEMP delete: Deleting Rest Consumer");
        try {
            restDeliveryPointApi.deleteMsgVpnRestDeliveryPointRestConsumer(request.getMsgVpn(), request.getRdpName(), request.getRestConsumerName());
        } catch (ApiException e) {
            handleSempOperationException(e, "Rest Consumer", supportedSempCommand());
        }
    }

    private void executeDeleteRdpOauthJwtClaims(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpOauthJwtClaimDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempRdpOauthJwtClaimDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getRestConsumerName(), "Rest Consumer name must not be empty");
        Validate.notEmpty(request.getOauthJwtClaimName(), "OAuth JWT claim name must not be empty");
        log.info("SEMP delete: Deleting Oauth JWT Claim");
        try {
            restDeliveryPointApi.deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(
                    request.getMsgVpn(),
                    request.getRdpName(),
                    request.getRestConsumerName(),
                    request.getOauthJwtClaimName()
            );
        } catch (ApiException e) {
            handleSempOperationException(e, "Oauth JWT Claim", supportedSempCommand());
        }
    }

    private void executeRdpQueueBinding(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpQueueBindingDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempRdpQueueBindingDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getQueueBindingName(), "Queue binding name must not be empty");
        log.info("SEMP delete: Deleting Queue Binding");
        try {
            restDeliveryPointApi.deleteMsgVpnRestDeliveryPointQueueBinding(request.getMsgVpn(), request.getRdpName(), request.getQueueBindingName());
        } catch (ApiException e) {
            handleSempOperationException(e, "Queue Binding", supportedSempCommand());
        }
    }

    private void executeRdpQueueBindingRequestHeader(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpQueueBindingRequestHeaderDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempRdpQueueBindingRequestHeaderDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getQueueBindingName(), "Queue binding name must not be empty");
        if (request.isProtected()) {
            log.info("SEMP delete: Deleting protected request header");
            try {
                restDeliveryPointApi.deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader(request.getMsgVpn(), request.getRdpName(),
                        request.getQueueBindingName(), request.getHeaderName());
            } catch (ApiException e) {
                handleSempOperationException(e, "Protected Request Header", supportedSempCommand());
            }
        } else {
            log.info("SEMP delete: Deleting request header");
            try {
                restDeliveryPointApi.deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(request.getMsgVpn(), request.getRdpName(),
                        request.getQueueBindingName(), request.getHeaderName());
            } catch (ApiException e) {
                handleSempOperationException(e, "Request Header", supportedSempCommand());
            }
        }

    }
}

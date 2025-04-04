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
import com.solace.maas.ep.common.model.SempRdpQueueBindingDeletionRequest;
import com.solace.maas.ep.common.model.SempRdpQueueBindingRequestHeaderDeletionRequest;
import com.solace.maas.ep.common.model.SempRdpRestConsumerDeletionRequest;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants.SEMP_DELETE_DATA;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.setCommandError;

@Service
@Slf4j
public class SempDeleteCommandManager {

    private final ObjectMapper objectMapper;

    public SempDeleteCommandManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void execute(Command command, SempApiProvider sempApiProvider) {
        try {
            validate(command, sempApiProvider);
            executeSempDeleteCommand(command, sempApiProvider);
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .logs(List.of())
                    .build());
            // not found is not an error and is already handled in handleSempApiDeleteException
            // all other exceptions are considered errors and are (re)-thrown to be handled here
        } catch (Exception e) {
            log.error("SEMP delete command not executed successfully", e);
            // SEMP APIExceptions don't expose the response body via e.getMessage()
            setCommandError(command, e);
        }
    }

    private void executeSempDeleteCommand(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        String entityType = (String) command.getParameters().get(SempDeleteCommandConstants.SEMP_DELETE_ENTITY_TYPE);
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
                executeDeleteClientUsername(command, sempApiProvider);
                break;
            case solaceAuthorizationGroup:
                executeDeleteAuthorizationGroup(command, sempApiProvider);
                break;
            case solaceAclSubscribeTopicException:
                executeDeleteAclSubscribeTopicException(command, sempApiProvider);
                break;
            case solaceRDP:
                executeDeleteRdp(command, sempApiProvider);
                break;
            case solaceRDPRestConsumer:
                executeDeleteRdpRestConsumer(command, sempApiProvider);
                break;
            case solaceRdpQueueBinding:
                executeRdpQueueBinding(command, sempApiProvider);
                break;
            case solaceRdpQueueBindingProtectedRequestHeader, solaceRdpQueueBindingRequestHeader:
                executeRdpQueueBindingRequestHeader(command, sempApiProvider);
                break;
            case solaceAclPublishTopicException:
                executeDeleteAclPublishTopicException(command, sempApiProvider);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported entity type for deletion: " + deletionEntityType);
        }
    }

    // Suppressing PMD of unused entityName
    // This is because we are not logging the entityName for security reasons now
    // We might adapt that in the future to log the entityName based on logging level and properties file
    @SuppressWarnings("PMD")
    private void handleSempApiDeleteException(ApiException e, String entityType, String entityName) throws ApiException {
        // If the entity does not exist, we don't want to consider it an error
        // This is because of the edge case of dangling entities from previous deployment that did not get finalized
        // SEMP does not report a 404 for entities that do not exist, so we have to check the response body
        // NOT_FOUND is the string that SEMP returns within the response JSON, when an entity does not exist
        if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
            // we don't want to log the content of the response body, as it may contain sensitive information
            // we only log the entity type. The entity name is also considered sensitive information and is not logged
            log.info("SEMP delete: tried to delete {} which did not exist (anymore)", entityType);
        } else {
            throw e;
        }
    }

    private void executeDeleteAclProfile(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AclProfileApi aclProfileApi = sempApiProvider.getAclProfileApi();
        SempAclProfileDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempAclProfileDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getAclProfileName(), "ACL profile name must not be empty");

        log.info("SEMP delete: Deleting ACL profile");
        try {
            aclProfileApi.deleteMsgVpnAclProfile(request.getMsgVpn(), request.getAclProfileName());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "ACL profile", request.getAclProfileName());
        }
    }

    private void executeDeleteAclPublishTopicException(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AclProfileApi aclProfileApi = sempApiProvider.getAclProfileApi();
        SempAclPublishTopicExceptionDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempAclPublishTopicExceptionDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getAclProfileName(), "ACL profile name must not be empty");
        Validate.notEmpty(request.getPublishTopic(), "Publish topic must not be empty");
        log.info("SEMP delete: Deleting ACL publish topic exception");
        try {
            aclProfileApi.deleteMsgVpnAclProfilePublishTopicException(request.getMsgVpn(), request.getAclProfileName(), "smf", request.getPublishTopic());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "ACL publish topic exception", request.getPublishTopic());
        }
    }

    private void executeDeleteAclSubscribeTopicException(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AclProfileApi aclProfileApi = sempApiProvider.getAclProfileApi();
        SempAclSubscribeTopicExceptionDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempAclSubscribeTopicExceptionDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getAclProfileName(), "ACL profile name must not be empty");
        Validate.notEmpty(request.getSubscribeTopic(), "Subscribe topic must not be empty");

        log.info("SEMP delete: Deleting ACL subscribe topic exception");
        try {
            aclProfileApi.deleteMsgVpnAclProfileSubscribeTopicException(request.getMsgVpn(), request.getAclProfileName(), "smf", request.getSubscribeTopic());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "ACL subscribe topic exception", request.getSubscribeTopic());
        }
    }

    private void executeDeleteAuthorizationGroup(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AuthorizationGroupApi authorizationGroupApi = sempApiProvider.getAuthorizationGroupApi();
        SempAuthorizationGroupDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempAuthorizationGroupDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getAuthorizationGroupName(), "Authorization group name must not be empty");

        log.info("SEMP delete: Deleting authorization group");
        try {
            authorizationGroupApi.deleteMsgVpnAuthorizationGroup(request.getMsgVpn(), request.getAuthorizationGroupName());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "Authorization group", request.getAuthorizationGroupName());
        }
    }

    private void executeDeleteClientUsername(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        ClientUsernameApi clientUsernameApi = sempApiProvider.getClientUsernameApi();
        SempClientUsernameDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempClientUsernameDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getClientUsername(), "Client username must not be empty");
        log.info("SEMP delete: Deleting client username");
        try {
            clientUsernameApi.deleteMsgVpnClientUsername(request.getMsgVpn(), request.getClientUsername());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "Client username", request.getClientUsername());
        }
    }

    private void executeDeleteSolaceQueue(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        QueueApi queueApi = sempApiProvider.getQueueApi();
        SempQueueDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempQueueDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getQueueName(), "Queue name must not be empty");
        log.info("SEMP delete: Deleting queue");
        try {
            queueApi.deleteMsgVpnQueue(request.getMsgVpn(), request.getQueueName());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "Queue", request.getQueueName());
        }
    }

    private void executeDeleteSolaceQueueTopicSubscription(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        QueueApi queueApi = sempApiProvider.getQueueApi();
        SempQueueTopicSubscriptionDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempQueueTopicSubscriptionDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getQueueName(), "Queue name must not be empty");
        Validate.notEmpty(request.getTopicName(), "Topic name must not be empty");
        log.info("SEMP delete: Deleting queue subscription");
        try {
            queueApi.deleteMsgVpnQueueSubscription(request.getMsgVpn(), request.getQueueName(), request.getTopicName());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "Subscription", request.getTopicName());
        }
    }


    private static void validate(Command command, SempApiProvider sempApiProvider) {
        Validate.isTrue(command.getCommandType().equals(CommandType.semp), "Command type must be semp");
        Validate.notNull(sempApiProvider, "SempApiProvider must not be null");
        Validate.notEmpty(command.getParameters(), "Command parameters must not be empty");
        Validate.notNull(command.getParameters().get(SempDeleteCommandConstants.SEMP_DELETE_ENTITY_TYPE), "Semp delete request must be against a specific " +
                "semp entity type");

    }

    private void executeDeleteRdp(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempRdpDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        log.info("SEMP delete: Deleting Rest Delivery Point");
        try {
            restDeliveryPointApi.deleteMsgVpnRestDeliveryPoint(request.getMsgVpn(), request.getRdpName());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "Rest Delivery Point", request.getRdpName());
        }
    }


    private void executeDeleteRdpRestConsumer(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpRestConsumerDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempRdpRestConsumerDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getRestConsumerName(), "Rest Consumer name must not be empty");
        log.info("SEMP delete: Deleting Rest Consumer");
        try {
            restDeliveryPointApi.deleteMsgVpnRestDeliveryPointRestConsumer(request.getMsgVpn(), request.getRdpName(), request.getRestConsumerName());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "Rest Consumer", request.getRdpName());
        }
    }

    private void executeRdpQueueBinding(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpQueueBindingDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempRdpQueueBindingDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getQueueBindingName(), "Queue binding name must not be empty");
        log.info("SEMP delete: Deleting Queue Binding");
        try {
            restDeliveryPointApi.deleteMsgVpnRestDeliveryPointQueueBinding(request.getMsgVpn(), request.getRdpName(), request.getQueueBindingName());
        } catch (ApiException e) {
            handleSempApiDeleteException(e, "Queue Binding", request.getRdpName());
        }
    }

    private void executeRdpQueueBindingRequestHeader(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpQueueBindingRequestHeaderDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempRdpQueueBindingRequestHeaderDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getQueueBindingName(), "Queue binding name must not be empty");
        if (request.isProtected()) {
            log.info("SEMP delete: Deleting protected request header");
            try {
                restDeliveryPointApi.deleteMsgVpnRestDeliveryPointQueueBindingProtectedRequestHeader(request.getMsgVpn(), request.getRdpName(),
                        request.getQueueBindingName(), request.getHeaderName());
            } catch (ApiException e) {
                handleSempApiDeleteException(e, "Protected Request Header", request.getHeaderName());
            }
        } else {
            log.info("SEMP delete: Deleting request header");
            try {
                restDeliveryPointApi.deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(request.getMsgVpn(), request.getRdpName(),
                        request.getQueueBindingName(), request.getHeaderName());
            } catch (ApiException e) {
                handleSempApiDeleteException(e, "Request Header", request.getHeaderName());
            }
        }

    }
}

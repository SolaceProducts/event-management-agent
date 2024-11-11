package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.AclProfileApi;
import com.solace.client.sempv2.api.AuthorizationGroupApi;
import com.solace.client.sempv2.api.ClientUsernameApi;
import com.solace.client.sempv2.api.QueueApi;
import com.solace.maas.ep.common.model.SempAclProfileDeletionRequest;
import com.solace.maas.ep.common.model.SempAclPublishTopicExceptionDeletionRequest;
import com.solace.maas.ep.common.model.SempAclSubscribeTopicExceptionDeletionRequest;
import com.solace.maas.ep.common.model.SempAuthorizationGroupDeletionRequest;
import com.solace.maas.ep.common.model.SempClientUsernameDeletionRequest;
import com.solace.maas.ep.common.model.SempEntityType;
import com.solace.maas.ep.common.model.SempQueueDeletionRequest;
import com.solace.maas.ep.common.model.SempQueueTopicSubscriptionDeletionRequest;
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

    public static final String ERROR_EXECUTING_COMMAND = "Error executing command";

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
        } catch(ApiException e) {
            ApiException apiException = (ApiException) e;
            log.error("Error executing SEMP delete command: {}", apiException.getResponseBody());
            setCommandError(command, e);
        } catch (Exception e) {
            log.error(ERROR_EXECUTING_COMMAND, e);
            setCommandError(command, e);
        }
    }


    private void executeSempDeleteCommand(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        SempEntityType deletionEntityType = SempEntityType.valueOf((String) command.getParameters().get(SempDeleteCommandConstants.SEMP_DELETE_ENTITY_TYPE));
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
            case solaceAclPublishTopicException:
                executeDeleteAclPublishTopicException(command, sempApiProvider);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported entity type for deletion: " + deletionEntityType);
        }
    }

    private void executeDeleteAclProfile(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AclProfileApi aclProfileApi = sempApiProvider.getAclProfileApi();
        SempAclProfileDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempAclProfileDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getAclProfileName(), "ACL profile name must not be empty");

        log.info("Deleting ACL profile via Semp V2 delete: {}", request.getAclProfileName());
        try {
            aclProfileApi.deleteMsgVpnAclProfile(request.getMsgVpn(), request.getAclProfileName());
        } catch (ApiException e) {
            // If the client username does not exist, we don't want to consider it an error
            // This is because of the edge case of dangling client usernames from previous deployment that did not get finalized
            // SEMP does not report a 404 for client usernames that do not exist, so we have to check the response body
            if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
                log.debug("Tried to delete ACL profile {} which did not exist - not considered an error", request.getAclProfileName());
            } else {
                throw e;
            }
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

        log.info("Deleting ACL subscribe topic exception via Semp V2 delete: {}", request.getPublishTopic());
        try {
            aclProfileApi.deleteMsgVpnAclProfilePublishTopicException(request.getMsgVpn(), request.getAclProfileName(),"smf", request.getPublishTopic());
        } catch (ApiException e) {
            // If the client username does not exist, we don't want to consider it an error
            // This is because of the edge case of dangling client usernames from previous deployment that did not get finalized
            // SEMP does not report a 404 for client usernames that do not exist, so we have to check the response body
            if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
                log.debug("Tried to delete ACL publish topic exception {} in ACL profile {}  which did not exist - not considered an error",
                        request.getPublishTopic(),request.getAclProfileName());
            } else {
                throw e;
            }
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

        log.info("Deleting ACL subscribe topic exception via Semp V2 delete: {}", request.getSubscribeTopic());
        try {
            aclProfileApi.deleteMsgVpnAclProfileSubscribeTopicException(request.getMsgVpn(), request.getAclProfileName(),"smf", request.getSubscribeTopic());
        } catch (ApiException e) {
            // If the client username does not exist, we don't want to consider it an error
            // This is because of the edge case of dangling client usernames from previous deployment that did not get finalized
            // SEMP does not report a 404 for client usernames that do not exist, so we have to check the response body
            if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
                log.debug("Tried to delete ACL subscribe topic exception {} in ACL profile {}  which did not exist - not considered an error",
                        request.getSubscribeTopic(),request.getAclProfileName());
            } else {
                throw e;
            }
        }
    }

    private void executeDeleteAuthorizationGroup(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        AuthorizationGroupApi authorizationGroupApi = sempApiProvider.getAuthorizationGroupApi();
        SempAuthorizationGroupDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempAuthorizationGroupDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getAuthorizationGroupName(), "Authorization group name must not be empty");

        log.info("Deleting authorization group via Semp V2 delete: {}", request.getAuthorizationGroupName());
        try {
            authorizationGroupApi.deleteMsgVpnAuthorizationGroup(request.getMsgVpn(), request.getAuthorizationGroupName());
        } catch (ApiException e) {
            // If the client username does not exist, we don't want to consider it an error
            // This is because of the edge case of dangling client usernames from previous deployment that did not get finalized
            // SEMP does not report a 404 for client usernames that do not exist, so we have to check the response body
            if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
                log.debug("Tried to delete authorization group {} which did not exist - not considered an error", request.getAuthorizationGroupName());
            } else {
                throw e;
            }
        }
    }

    private void executeDeleteClientUsername(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        ClientUsernameApi clientUsernameApi = sempApiProvider.getClientUsernameApi();
        SempClientUsernameDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempClientUsernameDeletionRequest.class);

        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getClientUsername(), "Client username must not be empty");

        log.info("Deleting client username via Semp V2 delete: {}", request.getClientUsername());
        try {
            clientUsernameApi.deleteMsgVpnClientUsername(request.getMsgVpn(), request.getClientUsername());
        } catch (ApiException e) {
            // If the client username does not exist, we don't want to consider it an error
            // This is because of the edge case of dangling client usernames from previous deployment that did not get finalized
            // SEMP does not report a 404 for client usernames that do not exist, so we have to check the response body
            if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
                log.debug("Tried to delete client username {} which did not exist - not considered an error", request.getClientUsername());
            } else {
                throw e;
            }
        }
    }
    private void executeDeleteSolaceQueue(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        QueueApi queueApi = sempApiProvider.getQueueApi();
        SempQueueDeletionRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_DELETE_DATA)),
                SempQueueDeletionRequest.class);
        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getQueueName(), "Queue name must not be empty");
        log.info("Deleting queue via Semp V2 delete: {}", request.getQueueName());
        try {
            queueApi.deleteMsgVpnQueue(request.getMsgVpn(), request.getQueueName());
        } catch (ApiException e) {
            // If the queue does not exist, we don't want to consider it an error
            // This is because of the edge case of dangling queues from previous deployment that did not get finalized
            // SEMP does not report a 404 for queues that do not exist, so we have to check the response body
            if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
                log.debug("Tried to delete queue {} which did not exist - not considered an error", request.getQueueName());
            } else {
                throw e;
            }
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
        log.info("Deleting subscription Via Semp V2 delete: {}", request.getTopicName());
        try {
            queueApi.deleteMsgVpnQueueSubscription(request.getMsgVpn(), request.getQueueName(), request.getTopicName());
        } catch (ApiException e) {
            // If the subscription does not exist, we don't want to consider it an error
            // This is because of the edge case of dangling subscriptions from previous deployment that did not get finalized
            // SEMP does not report a 404 for subscriptions that do not exist, so we have to check the response body
            if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
                log.debug("Tried to delete subscription {} which did not exist - not considered an error", request.getTopicName());
            } else {
                throw e;
            }
        }
    }


    private static void validate(Command command, SempApiProvider sempApiProvider) {
        Validate.isTrue(command.getCommandType().equals(CommandType.semp), "Command type must be semp");
        Validate.notNull(sempApiProvider, "SempApiProvider must not be null");
        Validate.notEmpty(command.getParameters(), "Command parameters must not be empty");
        Validate.notNull(command.getParameters().get(SempDeleteCommandConstants.SEMP_DELETE_ENTITY_TYPE), "Semp delete request must be against a specific " +
                "semp entity type");

    }



}

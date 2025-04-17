package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.RestDeliveryPointApi;
import com.solace.client.sempv2.model.MsgVpnRestDeliveryPointRestConsumer;
import com.solace.maas.ep.common.model.SempEntityType;
import com.solace.maas.ep.common.model.SempRdpRestConsumerPatchRequest;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempPatchCommandConstants.SEMP_PATCH_DATA;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempPatchCommandConstants.SEMP_PATCH_ENTITY_TYPE;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.setCommandError;

@Service
@Slf4j
public class SempPatchCommandManager {

    private final ObjectMapper objectMapper;

    public SempPatchCommandManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void execute(Command command, SempApiProvider sempApiProvider) {
        try {
            validate(command, sempApiProvider);
            executeSempPatchCommand(command, sempApiProvider);
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .logs(List.of())
                    .build());
        } catch (Exception e) {
            log.error("SEMP patch command not executed successfully", e);
            // SEMP APIExceptions don't expose the response body via e.getMessage()
            setCommandError(command, e);
        }
    }

    private void executeSempPatchCommand(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        String entityType = (String) command.getParameters().get(SEMP_PATCH_ENTITY_TYPE);
        SempEntityType patchEntityType;
        if (entityType == null) {
            throw new IllegalArgumentException("Entity type of a SEMP patch command must not be null");
        }
        try {
            patchEntityType = SempEntityType.valueOf(entityType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported SEMP patch entity type:" + entityType);
        }
        switch (patchEntityType) {
            case solaceRdpRestConsumer:
                executePatchRdpRestConsumer(command, sempApiProvider);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported entity type for patch: " + patchEntityType);
        }
    }

    // Suppressing PMD of unused entityName
    // This is because we are not logging the entityName for security reasons now
    // We might adapt that in the future to log the entityName based on logging level and properties file
    @SuppressWarnings("PMD")
    private void handleSempApiPatchException(ApiException e, String entityType) throws ApiException {
        // If the entity does not exist, we don't want to consider it an error
        // This is because of the edge case of dangling entities from previous deployment that did not get finalized
        // SEMP does not report a 404 for entities that do not exist, so we have to check the response body
        // NOT_FOUND is the string that SEMP returns within the response JSON, when an entity does not exist
        if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
            // we don't want to log the content of the response body, as it may contain sensitive information
            // we only log the entity type. The entity name is also considered sensitive information and is not logged
            log.info("SEMP delete: tried to patch {} which did not exist (anymore)", entityType);
        } else {
            throw e;
        }
    }


    private static void validate(Command command, SempApiProvider sempApiProvider) {
        Validate.isTrue(command.getCommandType().equals(CommandType.semp), "Command type must be semp");
        Validate.notNull(sempApiProvider, "SempApiProvider must not be null");
        Validate.notEmpty(command.getParameters(), "Command parameters must not be empty");
        Validate.notNull(command.getParameters().get(SEMP_PATCH_ENTITY_TYPE), "Semp patch request must be against a specific " +
                "semp entity type");
        Validate.notNull(command.getParameters().get(SEMP_PATCH_DATA), "Semp patch request must contain data");

    }

    private void executePatchRdpRestConsumer(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpRestConsumerPatchRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SEMP_PATCH_DATA)),
                SempRdpRestConsumerPatchRequest.class);
        Validate.notNull(request.getData(), "Semp patch request data must not be null");
        Validate.notEmpty(request.getMsgVpn(), "Msg VPN must not be empty");
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getRestConsumerName(), "Rest Consumer name must not be empty");
        log.info("SEMP patch command: Patching Rest Consumer");
        try {
            MsgVpnRestDeliveryPointRestConsumer toPatch = request.getData();
            restDeliveryPointApi.updateMsgVpnRestDeliveryPointRestConsumer(
                    request.getMsgVpn(),
                    request.getRdpName(),
                    request.getRestConsumerName(),
                    toPatch,
                    null,
                    null
            );
        } catch (ApiException e) {
            handleSempApiPatchException(e, "Rest Consumer");
        }
    }

}

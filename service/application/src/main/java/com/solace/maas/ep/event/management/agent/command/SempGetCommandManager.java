package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.ClientProfileApi;
import com.solace.maas.ep.common.model.SempClientProfileValidationRequest;
import com.solace.maas.ep.common.model.SempEntityType;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.setCommandError;

@Service
@Slf4j
public class SempGetCommandManager extends AbstractSempCommandManager {
    private final ObjectMapper objectMapper;

    public SempGetCommandManager(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    @Override
    public String supportedSempCommand() {
        return SempCommandConstants.SEMP_GET_OPERATION;
    }

    /**
     * Executes a SEMP GET command against the broker.
     * Specifically handles client profile validation by checking if a profile exists.
     * Sets appropriate status and error messages based on the result.
     *
     * @param command         The command to execute
     * @param sempApiProvider The provider for SEMP APIs
     */
    @Override
    public void execute(Command command, SempApiProvider sempApiProvider) {
        try {
            validate(command, sempApiProvider);
            executeSempCommand(command, sempApiProvider);
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .logs(List.of(Map.of(
                            "message", "Resource found",
                            "level", "INFO",
                            "timestamp", OffsetDateTime.now()
                    )))
                    .build());
        } catch (ApiException e) {
            String entityType = (String) command.getParameters().get(SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE);
            if (SempEntityType.solaceClientProfileName.name().equals(entityType) &&
                    isResourceNotFoundError(e)) {
                handleClientProfileNotFound(command);
            } else {
                log.error("SEMP {} command not executed successfully", supportedSempCommand(), e);
                setCommandError(command, e);
            }
        } catch (Exception e) {
            log.error("SEMP {} command not executed successfully", supportedSempCommand(), e);
            setCommandError(command, e);
        }
    }

    private boolean isResourceNotFoundError(ApiException e) {
        return e.getCode() == 404 ||
                (e.getCode() == 400 && StringUtils.contains(e.getResponseBody(), "NOT_FOUND"));
    }

    private void handleClientProfileNotFound(Command command) {
        String resourceName = extractClientProfileName(command);
        String errorMessage = String.format("Named client profile not found on the event broker: %s", resourceName);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(SempCommandConstants.VALIDATION_ERROR_MESSAGE, errorMessage);
        log.warn(errorMessage);

        command.setIgnoreResult(false);
        command.setResult(CommandResult.builder()
                .status(JobStatus.validation_error)
                .result(resultMap)
                .logs(List.of(Map.of(
                        "message", errorMessage,
                        "level", "WARN",
                        "timestamp", OffsetDateTime.now()
                )))
                .build());
    }

    private String extractClientProfileName(Command command) {
        try {
            Object data = command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA);
            if (data != null) {
                SempClientProfileValidationRequest request = objectMapper.readValue(
                        objectMapper.writeValueAsString(data),
                        SempClientProfileValidationRequest.class);
                return request.getClientProfileName();
            }
        } catch (Exception e) {
            log.warn("Failed to extract resource name from SEMP {} command", supportedSempCommand(), e);
        }
        return "unknown";
    }

    @Override
    protected void executeSempCommand(Command command, SempApiProvider sempApiProvider) throws Exception {
        String entityType = (String) command.getParameters().get(SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE);
        SempEntityType getEntityType;

        if (entityType == null) {
            throw new IllegalArgumentException("Entity type of a SEMP get command must not be null");
        }

        try {
            getEntityType = SempEntityType.valueOf(entityType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported SEMP get entity type: " + entityType);
        }

        switch (getEntityType) {
            case solaceClientProfileName:
                executeGetClientProfile(command, sempApiProvider);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported entity type for get: " + getEntityType);
        }
    }

    private void executeGetClientProfile(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        ClientProfileApi clientProfileApi = sempApiProvider.getClientProfileApi();

        SempClientProfileValidationRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempClientProfileValidationRequest.class);

        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getClientProfileName(), "Client profile name must not be empty");

        log.debug("SEMP {} command: Checking client profile name existence", supportedSempCommand());

        // This will throw ApiException if the client profile doesn't exist
        clientProfileApi.getMsgVpnClientProfile(
                request.getMsgVpn(),
                request.getClientProfileName(),
                null,  // opaquePassword
                null // select
        );
    }
}

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
import com.solace.maas.ep.event.management.agent.plugin.command.model.FailureSeverity;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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
     * Overrides the execute method to handle the pre-flight check with special logic for warnings
     */
    @Override
    public void execute(Command command, SempApiProvider sempApiProvider) {
        try {
            validate(command, sempApiProvider);
            executeSempCommand(command, sempApiProvider);
            // If we get here, the get operation was successful
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .logs(List.of(Map.of(
                            "message", "Resource found",
                            "level", "INFO",
                            "timestamp", OffsetDateTime.now()
                    )))
                    .build());
        } catch (ApiException e) {
            // For 404 errors in a pre-flight check, use warning instead of error when appropriate
            if (e.getCode() == 404 || (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND"))) {
                if (Boolean.TRUE.equals(command.getIsPreFlightCheck()) &&
                        command.getFailureSeverity() == FailureSeverity.WARNING) {
                    // Extract client profile name for better error messaging
                    String resourceName = extractResourceName(command);
                    log.warn("Pre-flight check: Resource not found: {}", resourceName);
                    command.setResult(CommandResult.builder()
                            .status(JobStatus.warning)
                            .logs(List.of(Map.of(
                                    "message", "Required resource not found: " + resourceName,
                                    "level", "WARN",
                                    "timestamp", OffsetDateTime.now()
                            )))
                            .build());
                    return;
                }
            }
            log.error("SEMP {} command not executed successfully", supportedSempCommand(), e);
            // Set error for all other API exceptions
            setCommandError(command, e);
        } catch (Exception e) {
            log.error("SEMP {} command not executed successfully", supportedSempCommand(), e);
            setCommandError(command, e);
        }
    }

    /**
     * Extracts the resource name from the command for better error messages
     */
    private String extractResourceName(Command command) {
        try {
            Object data = command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA);
            if (data != null) {
                // Convert to string and parse as SempClientProfileValidationRequest
                SempClientProfileValidationRequest request = objectMapper.readValue(
                        objectMapper.writeValueAsString(data),
                        SempClientProfileValidationRequest.class);
                return request.getClientProfileName();
            }
        } catch (Exception e) {
            log.warn("Failed to extract resource name from command", e);
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
            case solaceClientProfile:
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

        log.info("SEMP get: Checking client profile existence");

        // This will throw ApiException if the client profile doesn't exist
        clientProfileApi.getMsgVpnClientProfile(
                request.getMsgVpn(),
                request.getClientProfileName(),
                null,  // select
                null   // opaquePassword
        );

        // If we get here, the client profile exists
    }
}

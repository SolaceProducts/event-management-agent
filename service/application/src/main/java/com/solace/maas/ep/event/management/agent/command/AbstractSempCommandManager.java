package com.solace.maas.ep.event.management.agent.command;

import com.solace.client.sempv2.ApiException;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;

import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.setCommandError;

@Slf4j
public abstract class AbstractSempCommandManager {
    public static final String MSG_VPN_EMPTY_ERROR_MSG = "Msg VPN must not be empty";

    public abstract String supportedSempCommand();

    public void execute(Command command, SempApiProvider sempApiProvider) {
        try {
            validate(command, sempApiProvider);
            executeSempCommand(command, sempApiProvider);
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .logs(List.of())
                    .build());
            // not found is not an error and is already handled in handleSempApiDeleteException
            // all other exceptions are considered errors and are (re)-thrown to be handled here
        } catch (Exception e) {
            log.error("SEMP {} command not executed successfully", supportedSempCommand(), e);
            // SEMP APIExceptions don't expose the response body via e.getMessage()
            setCommandError(command, e);
        }
    }

    protected abstract void executeSempCommand(Command command, SempApiProvider sempApiProvider) throws Exception;


    public void validate(Command command, SempApiProvider sempApiProvider) {
        Validate.isTrue(command.getCommand().equals(supportedSempCommand()), "Command must be " + supportedSempCommand());
        Validate.isTrue(command.getCommandType().equals(CommandType.semp), "Command type must be semp");
        Validate.notNull(sempApiProvider, "SempApiProvider must not be null");
        Validate.notEmpty(command.getParameters(), "Command parameters must not be empty");
        Validate.notNull(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE), "Semp request must be against a specific " +
                "semp entity type");
        Validate.notNull(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA), "Semp request must contain data");
    }

    public void handleSempOperationException(ApiException e,
                                             String entityType,
                                             String command) throws ApiException {
        // If the entity does not exist, we don't want to consider it an error
        // This is because of the edge case of dangling entities from previous deployment that did not get finalized
        // SEMP does not report a 404 for entities that do not exist, so we have to check the response body
        // NOT_FOUND is the string that SEMP returns within the response JSON, when an entity does not exist
        if (e.getCode() == 400 && e.getResponseBody().contains("NOT_FOUND")) {
            // we don't want to log the content of the response body, as it may contain sensitive information
            // we only log the entity type. The entity name is also considered sensitive information and is not logged
            log.info("SEMP {}: tried to {} {} which did not exist (anymore)", command, command, entityType);
        } else {
            throw e;
        }
    }
}

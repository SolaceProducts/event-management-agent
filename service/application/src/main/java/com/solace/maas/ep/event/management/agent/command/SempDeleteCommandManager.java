package com.solace.maas.ep.event.management.agent.command;

import com.solace.client.sempv2.ApiClient;
import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.QueueApi;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempEntityType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempQueueDeletionRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempTopicSubscriptionDeletionRequest;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempDeleteCommandConstants.SEMP_DELETE_DATA;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils.setCommandError;

@Service
@Slf4j
public class SempDeleteCommandManager {
    public static final String ERROR_EXECUTING_COMMAND = "Error executing command";

    public void execute(Command command, SolaceHttpSemp solaceClient) {
        try {
            validate(command, solaceClient);
            setupApiClient(solaceClient);
            executeSempDeleteCommand(command, solaceClient);
        } catch (Exception e) {
            log.error(ERROR_EXECUTING_COMMAND, e);
            setCommandError(command, e);
        }
    }


    public void executeSempDeleteCommand(Command command, SolaceHttpSemp solaceClient) throws ApiException {
        validate(command, solaceClient);
        ApiClient apiClient = setupApiClient(solaceClient);
        QueueApi queueApi = new QueueApi(apiClient);

        SempEntityType deletionEntityType = (SempEntityType) command.getParameters().get(SempDeleteCommandConstants.SEMP_DELETE_ENTITY_TYPE);

        if (deletionEntityType == SempEntityType.solaceQueue) {
            SempQueueDeletionRequest request = (SempQueueDeletionRequest) command.getParameters().get(SEMP_DELETE_DATA);
            queueApi.deleteMsgVpnQueue(request.getQueueName(), request.getMsgVpn());
        } else if (deletionEntityType == SempEntityType.subscriptionTopic) {
            SempTopicSubscriptionDeletionRequest request = (SempTopicSubscriptionDeletionRequest) command.getParameters().get(SEMP_DELETE_DATA);
            queueApi.deleteMsgVpnQueueSubscription(request.getMsgVpn(), request.getQueueName(), request.getTopicName());
        } else {
            throw new UnsupportedOperationException("Unsupported entity type for deletion: " + deletionEntityType);
        }
    }


    private static void validate(Command command, SolaceHttpSemp solaceClient) {
        Validate.isTrue(command.getCommandType().equals(CommandType.semp), "Command type must be semp");
        Validate.notNull(solaceClient, "Solace client must not be null");
        Validate.notNull(solaceClient.getSempClient(), "Solace semp client must not be null");
        Validate.notEmpty(command.getParameters(), "Command parameters must not be empty");
        Validate.notNull(command.getParameters().get(SempDeleteCommandConstants.SEMP_DELETE_ENTITY_TYPE), "Semp delete request must be against a specific " +
                "semp entity type");

    }

    private ApiClient setupApiClient(SolaceHttpSemp solaceClient) {
        SempClient sempClient = solaceClient.getSempClient();
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(sempClient.getConnectionUrl());
        apiClient.setUsername(sempClient.getUsername());
        apiClient.setPassword(sempClient.getPassword());
        return apiClient;
    }

}

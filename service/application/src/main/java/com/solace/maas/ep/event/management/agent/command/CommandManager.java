package com.solace.maas.ep.event.management.agent.command;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CommandManager {
    private final TerraformManager terraformManager;

    private final MessagingServiceDelegateService messagingServiceDelegateService;

    public CommandManager(TerraformManager terraformManager, MessagingServiceDelegateService messagingServiceDelegateService) {
        this.terraformManager = terraformManager;
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    public void execute(CommandRequest request) {
        Map<String, String> envVars = setBrokerSpecificEnvVars(request.getMessagingServiceId());
        for (CommandBundle bundle : request.getCommandBundles()) {
            // For now everything is run serially
            for (Command command : bundle.getCommands()) {
                switch (command.getCommandType()) {
                    case terraform:
                        terraformManager.execute(request, command, envVars);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + command.getCommandType());
                }
            }
        }
    }

    private Map<String, String> setBrokerSpecificEnvVars(String messagingServiceId) {
        Map<String, String> envVars = new HashMap<>();
        Object client = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);
        if (client instanceof SolaceHttpSemp) {
            SolaceHttpSemp solaceClient = (SolaceHttpSemp) client;
            SempClient sempClient = solaceClient.getSempClient();
            envVars.put("TF_VAR_username", sempClient.getUsername());
            envVars.put("TF_VAR_password", sempClient.getPassword());
            envVars.put("TF_VAR_url", sempClient.getConnectionUrl());
        }
        return envVars;
    }
}

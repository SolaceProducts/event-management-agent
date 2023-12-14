package com.solace.maas.ep.event.management.agent.command;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.command.mapper.CommandMapper;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import com.solace.maas.ep.event.management.agent.util.MdcTaskDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.solace.maas.ep.event.management.agent.constants.Command.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager.LOG_LEVEL_ERROR;
import static com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager.setCommandError;

@Slf4j
@Service
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandManager {
    private final TerraformManager terraformManager;
    private final CommandMapper commandMapper;
    private final CommandPublisher commandPublisher;
    private final MessagingServiceDelegateService messagingServiceDelegateService;
    private final EventPortalProperties eventPortalProperties;
    private final ThreadPoolTaskExecutor configPushPool;

    public CommandManager(TerraformManager terraformManager, CommandMapper commandMapper,
                          CommandPublisher commandPublisher, MessagingServiceDelegateService messagingServiceDelegateService,
                          EventPortalProperties eventPortalProperties) {
        this.terraformManager = terraformManager;
        this.commandMapper = commandMapper;
        this.commandPublisher = commandPublisher;
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.eventPortalProperties = eventPortalProperties;
        configPushPool = new ThreadPoolTaskExecutor();
        configPushPool.setCorePoolSize(eventPortalProperties.getCommandThreadPoolMinSize());
        configPushPool.setMaxPoolSize(eventPortalProperties.getCommandThreadPoolMaxSize());
        configPushPool.setQueueCapacity(eventPortalProperties.getCommandThreadPoolQueueSize());
        configPushPool.setThreadNamePrefix("config-push-pool-");
        configPushPool.setTaskDecorator(new MdcTaskDecorator());
        configPushPool.initialize();
    }

    public void execute(CommandMessage request) {

        CompletableFuture.runAsync(() -> configPush(request), configPushPool)
                .exceptionally(e -> {
                    log.error("Error running command", e);
                    Command firstCommand = request.getCommandBundles().get(0).getCommands().get(0);
                    setCommandError(firstCommand, (Exception) e);
                    sendResponse(request);
                    return null;
                });
    }

    public void configPush(CommandMessage request) {
        Map<String, String> envVars;
        try {
            envVars = setBrokerSpecificEnvVars(request.getServiceId());
        } catch (Exception e) {
            log.error("Error getting terraform variables", e);
            Command firstCommand = request.getCommandBundles().get(0).getCommands().get(0);
            setCommandError(firstCommand, e);
            sendResponse(request);
            return;
        }

        for (CommandBundle bundle : request.getCommandBundles()) {
            // For now everything is run serially
            for (Command command : bundle.getCommands()) {
                try {
                    switch (command.getCommandType()) {
                        case terraform:
                            terraformManager.execute(commandMapper.map(request), command, envVars);
                            break;
                        default:
                            command.setResult(CommandResult.builder()
                                    .status(JobStatus.error)
                                    .logs(List.of(
                                            Map.of("message", "unknown command type " + command.getCommandType(),
                                                    "errorType", "UnknownCommandType",
                                                    "level", LOG_LEVEL_ERROR,
                                                    "timestamp", OffsetDateTime.now())))
                                    .build());
                            break;
                    }
                } catch (Exception e) {
                    log.error("Error executing command", e);
                    setCommandError(command, e);
                }
            }
        }
        sendResponse(request);
    }

    private void sendResponse(CommandMessage request) {
        Map<String, String> topicVars = Map.of(
                "orgId", request.getOrgId(),
                "runtimeAgentId", eventPortalProperties.getRuntimeAgentId(),
                COMMAND_CORRELATION_ID, request.getCommandCorrelationId()
        );
        commandPublisher.sendCommandResponse(request, topicVars);
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

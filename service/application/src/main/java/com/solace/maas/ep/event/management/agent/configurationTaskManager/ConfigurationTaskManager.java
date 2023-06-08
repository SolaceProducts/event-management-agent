package com.solace.maas.ep.event.management.agent.configurationTaskManager;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.configurationTaskManager.model.ConfigurationTaskBO;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.service.ConfigurationTaskService;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConfigurationTaskManager {

    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;
    private final ConfigurationTaskService configurationTaskService;
    private final String runtimeAgentId;

    @Autowired
    public ConfigurationTaskManager(MessagingServiceDelegateServiceImpl messagingServiceDelegateService,
                                    ConfigurationTaskService configurationTaskService, EventPortalProperties eventPortalProperties) {
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.configurationTaskService = configurationTaskService;
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    public boolean execute(ConfigurationTaskBO configurationTaskCommandBO) {
        String messagingServiceId = configurationTaskCommandBO.getMessagingServiceId();
        String commandId = configurationTaskCommandBO.getId();
        String groupId = UUID.randomUUID().toString();

        MDC.put(RouteConstants.TASK_ID, commandId);
        MDC.put(RouteConstants.SCHEDULE_ID, groupId);
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);

        MessagingServiceEntity messagingServiceEntity = retrieveMessagingServiceEntity(messagingServiceId);

        MessagingServiceRouteDelegate configurationDelegate =
                PluginLoader.findPlugin(configurationTaskCommandBO.getConfigType());
        Objects.requireNonNull(configurationDelegate,
                String.format("Unable to find messaging service plugin for plugin type %s. Valid types are %s.",
                        configurationTaskCommandBO.getConfigType(),
                        String.join(", ", PluginLoader.getKeys())));
        List<String> objectTypes = configurationTaskCommandBO.getTaskConfigs().stream().map(s->{
           return s.getObjectType();
        }).collect(Collectors.toUnmodifiableList());
        List<RouteBundle> routes = configurationTaskCommandBO.getDestinations().stream()
                .map(s -> {
                    return PluginLoader.findPlugin(configurationTaskCommandBO.getConfigType());
                })
                .filter(Objects::nonNull)
                .map(delegate -> delegate.generateRouteList(
                                List.of(),
                                List.of(),
                                objectTypes.stream().findFirst().orElseThrow(),
                                messagingServiceId
                        ).stream()
                        .findFirst()
                        .orElseThrow())
                .collect(Collectors.toUnmodifiableList());


        return this.configurationTaskService.execute(
                routes,
                groupId,
                configurationTaskCommandBO.getId(),
                configurationTaskCommandBO.getTaskConfigs()
        );
    }

    private MessagingServiceEntity retrieveMessagingServiceEntity(String messagingServiceId) {
        return messagingServiceDelegateService.getMessagingServiceById(messagingServiceId);
    }
}

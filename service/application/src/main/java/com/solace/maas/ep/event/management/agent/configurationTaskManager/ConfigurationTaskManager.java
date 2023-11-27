package com.solace.maas.ep.event.management.agent.configurationTaskManager;

import com.solace.maas.ep.event.management.agent.configurationTaskManager.model.ConfigurationTaskBO;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.event.management.agent.service.ConfigurationTaskService;
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

    private final ConfigurationTaskService configurationTaskService;

    @Autowired
    public ConfigurationTaskManager(ConfigurationTaskService configurationTaskService) {
        this.configurationTaskService = configurationTaskService;
    }

    public boolean execute(ConfigurationTaskBO configurationTaskCommandBO) {
        String messagingServiceId = configurationTaskCommandBO.getMessagingServiceId();
        String commandId = configurationTaskCommandBO.getId();
        String groupId = UUID.randomUUID().toString();

        MDC.put(RouteConstants.CONFIG_TASK_ID, commandId);
        MDC.put(RouteConstants.SCHEDULE_ID, groupId);
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);

        MessagingServiceRouteDelegate configurationDelegate =
                PluginLoader.findPlugin(configurationTaskCommandBO.getConfigType());
        Objects.requireNonNull(configurationDelegate,
                String.format("Unable to find messaging service plugin for plugin type %s. Valid types are %s.",
                        configurationTaskCommandBO.getConfigType(),
                        String.join(", ", PluginLoader.getKeys())));
        List<String> objectTypes = configurationTaskCommandBO.getTaskConfigs().stream().map(s -> {
            return s.getObjectType();
        }).collect(Collectors.toUnmodifiableList());
        List<RouteBundle> destinations = List.of(PluginLoader.findPlugin("CONFIGURATION_TASK_RESULT_PUBLISHER")).stream()
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
        List<RouteBundle> routes =
                List.of(PluginLoader.findPlugin(configurationTaskCommandBO.getConfigType())).stream().filter(Objects::nonNull)
                        .map(delegate -> delegate.generateRouteList(
                                        destinations,
                                        List.of(),
                                        objectTypes.stream().findFirst().orElseThrow(),
                                        messagingServiceId
                                ).stream()
                                .findFirst()
                                .orElseThrow())
                        .collect(Collectors.toUnmodifiableList());


        return configurationTaskService.execute(
                routes,
                groupId,
                configurationTaskCommandBO.getId(),
                configurationTaskCommandBO.getTaskConfigs()
        );
    }
}

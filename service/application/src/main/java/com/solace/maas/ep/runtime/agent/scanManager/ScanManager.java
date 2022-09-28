package com.solace.maas.ep.runtime.agent.scanManager;

import com.solace.maas.ep.runtime.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.runtime.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.runtime.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.runtime.agent.service.ScanService;
import com.solace.maas.ep.runtime.agent.service.logging.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScanManager {

    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;
    private final ScanService scanService;
    private final LoggingService loggingService;

    @Autowired
    public ScanManager(MessagingServiceDelegateServiceImpl messagingServiceDelegateService,
                       ScanService scanService, LoggingService loggingService) {
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.scanService = scanService;
        this.loggingService = loggingService;
    }

    public String scan(ScanRequestBO scanRequestBO) {
        String messagingServiceId = scanRequestBO.getMessagingServiceId();
        String scanId = scanRequestBO.getScanId();
        String groupId = UUID.randomUUID().toString();

        MessagingServiceEntity messagingServiceEntity = retrieveMessagingServiceEntity(messagingServiceId);

        MessagingServiceRouteDelegate scanDelegate =
                PluginLoader.findPlugin(messagingServiceEntity.getMessagingServiceType());

        Objects.requireNonNull(scanDelegate,
                String.format("Unable to find messaging service plugin for plugin type %s. Valid types are %s.",
                        messagingServiceEntity.getMessagingServiceType(),
                        String.join(", ", PluginLoader.getKeys())));

        List<String> scanDestinations = scanRequestBO.getDestinations();
        List<RouteBundle> destinations = scanDestinations.stream()
                .map(PluginLoader::findPlugin)
                .filter(Objects::nonNull)
                .map(delegate -> delegate.generateRouteList(
                                List.of(),
                                List.of(),
                                scanRequestBO.getScanTypes().stream().findFirst().orElseThrow(),
                                messagingServiceId
                        ).stream()
                        .findFirst()
                        .orElseThrow())
                .collect(Collectors.toUnmodifiableList());

        List<String> brokerScanTypes = scanRequestBO.getScanTypes();
        List<RouteBundle> routes = brokerScanTypes.stream()
                .distinct()
                .flatMap(brokerScanType -> scanDelegate.generateRouteList(destinations, List.of(),
                                brokerScanType, messagingServiceId)
                        .stream())
                .collect(Collectors.toUnmodifiableList());

        loggingService.prepareLoggers(groupId, scanId, messagingServiceEntity.getId());

        return scanService.singleScan(routes, routes.size(), groupId, scanId);
    }

    private MessagingServiceEntity retrieveMessagingServiceEntity(String messagingServiceId) {
        return messagingServiceDelegateService.getMessagingServiceById(messagingServiceId);
    }
}

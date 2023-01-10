package com.solace.maas.ep.event.management.agent.scanManager;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final String runtimeAgentId;

    @Autowired
    public ScanManager(MessagingServiceDelegateServiceImpl messagingServiceDelegateService,
                       ScanService scanService, EventPortalProperties eventPortalProperties) {
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.scanService = scanService;
        this.runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    public String scan(ScanRequestBO scanRequestBO) {
        String messagingServiceId = scanRequestBO.getMessagingServiceId();
        String scanId = scanRequestBO.getScanId();
        String groupId = UUID.randomUUID().toString();

        MDC.put(RouteConstants.SCAN_ID, scanId);
        MDC.put(RouteConstants.SCHEDULE_ID, groupId);
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);

        MessagingServiceEntity messagingServiceEntity = retrieveMessagingServiceEntity(messagingServiceId);

        MessagingServiceRouteDelegate scanDelegate =
                PluginLoader.findPlugin(messagingServiceEntity.getType());

        Objects.requireNonNull(scanDelegate,
                String.format("Unable to find messaging service plugin for plugin type %s. Valid types are %s.",
                        messagingServiceEntity.getType(),
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

        return scanService.singleScan(routes, groupId, scanId, messagingServiceEntity, runtimeAgentId);
    }

    private MessagingServiceEntity retrieveMessagingServiceEntity(String messagingServiceId) {
        return messagingServiceDelegateService.getMessagingServiceById(messagingServiceId);
    }

    public List<ScanItemBO> listScans() {
        return scanService.listScans();
    }

    public Page<ScanItemBO> findAll(Pageable pageable) {
        return scanService.findAll(pageable);
    }

    public Page<ScanItemBO> findByMessagingServiceId(String messagingServiceId, Pageable pageable) {
        return scanService.findByMessagingServiceId(messagingServiceId, pageable);
    }
}

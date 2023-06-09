package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScanServiceHelper {
    RouteBundle buildRouteBundle(String scaType, List<RouteBundle> destinations, List<RouteBundle> recipients) {
        return RouteBundle.builder()
                .messagingServiceId("service1")
                .routeId("route1")
                .scanType(scaType)
                .destinations(destinations)
                .recipients(recipients)
                .build();
    }

    RouteEntity buildRouteEntity(String routeId, String childRouteIds, boolean isActive) {
        return RouteEntity.builder()
                .id(routeId)
                .childRouteIds(childRouteIds)
                .active(isActive)
                .build();
    }

    ScanEntity buildScanEntity(String id, String emaId, List<RouteEntity> routes, MessagingServiceEntity messagingService) {
        return ScanEntity.builder()
                .id(id)
                .emaId(emaId)
                .route(routes)
                .messagingService(messagingService)
                .build();

    }

    ScanTypeEntity buildScanTypeEntity(String id, String name, ScanEntity scan, ScanStatusEntity status) {
        return ScanTypeEntity.builder()
                .id(id)
                .name(name)
                .scan(scan)
                .status(status)
                .build();
    }

    MessagingServiceEntity buildMessagingServiceEntity(String id, String name, String type) {
        return MessagingServiceEntity.builder()
                .id(id)
                .name(name)
                .type(type)
                .build();
    }

    public ScanStatusEntity buildScanStatusEntity(String id, String status) {
        return ScanStatusEntity.builder()
                .id(id)
                .status(status)
                .build();
    }
}

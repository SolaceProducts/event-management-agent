package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundleRecipientsStore;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanDestinationEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientsPathEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanDestinationRepository;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientRepository;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanRouteService implements RouteManager {
    private final ScanDestinationRepository scanDestinationRepository;

    private final ScanRecipientRepository scanRecipientRepository;

    private final ScanRecipientStoreRepository scanRecipientStoreRepository;

    private final RouteService routeService;

    @Autowired
    public ScanRouteService(ScanDestinationRepository scanDestinationRepository,
                            ScanRecipientRepository scanRecipientRepository,
                            ScanRecipientStoreRepository scanRecipientStoreRepository,
                            RouteService routeService) {
        this.scanDestinationRepository = scanDestinationRepository;
        this.scanRecipientRepository = scanRecipientRepository;
        this.scanRecipientStoreRepository = scanRecipientStoreRepository;
        this.routeService = routeService;
    }

    public Iterable<ScanRecipientEntity> saveRecipients(List<ScanRecipientEntity> scanRecipientEntities) {
        return scanRecipientRepository.saveAll(scanRecipientEntities);
    }

    public ScanDestinationEntity saveDestination(ScanDestinationEntity scanDestinationEntity) {
        return scanDestinationRepository.save(scanDestinationEntity);
    }

    public Iterable<ScanDestinationEntity> saveDestinations(List<ScanDestinationEntity> scanDestinationEntities) {
        return scanDestinationRepository.saveAll(scanDestinationEntities);
    }

    public Iterable<ScanRecipientsPathEntity> saveRecipientsPaths(RouteBundleRecipientsStore routeBundleRecipients,
                                                                  ScanEntity scanEntity) {
        List<String> scanRecipientStoreValues =
                new ArrayList<>(routeBundleRecipients.getStore().values());

        List<ScanRecipientsPathEntity> scanRecipientsPathEntities =
                scanRecipientStoreValues.stream().map(value ->
                        ScanRecipientsPathEntity.builder()
                                .path(value)
                                .scan(scanEntity)
                                .build()
                ).collect(Collectors.toUnmodifiableList());

        return scanRecipientStoreRepository.saveAll(scanRecipientsPathEntities);
    }

    @Override
    public List<String> getRecipientList(String scanId, String routeId) {
        return scanRecipientRepository.findAllByScanIdAndRouteId(scanId, routeId)
                .stream()
                .map(ScanRecipientEntity::getRecipient)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<String> getDestinationList(String scanId, String routeId) {
        return scanDestinationRepository.findAllByScanIdAndRouteId(scanId, routeId)
                .stream()
                .map(ScanDestinationEntity::getDestination)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void setupRoute(String routeId) {
        routeService.setupRoute(routeId);
    }
}

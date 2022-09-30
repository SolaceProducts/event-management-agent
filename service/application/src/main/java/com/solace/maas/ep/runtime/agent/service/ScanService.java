package com.solace.maas.ep.runtime.agent.service;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.runtime.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanDestinationEntity;
import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanLifecycleEntity;
import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanRecipientEntity;
import com.solace.maas.ep.runtime.agent.repository.scan.ScanRepository;
import com.solace.maas.ep.runtime.agent.service.lifecycle.ScanLifecycleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;
import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.SCHEDULE_ID;

/**
 * Responsible for initiating and managing Messaging Service scans.
 */
@Slf4j
@Service
public class ScanService {
    private final ScanRepository repository;

    private final ScanRouteService scanRouteService;

    private final RouteService routeService;

    private final ProducerTemplate producerTemplate;

    private final ScanLifecycleService scanLifecycleService;

    public ScanService(ScanRepository repository, ScanRouteService scanRouteService,
                       RouteService routeService, ProducerTemplate producerTemplate,
                       ScanLifecycleService scanLifecycleService) {
        this.repository = repository;
        this.scanRouteService = scanRouteService;
        this.routeService = routeService;
        this.producerTemplate = producerTemplate;
        this.scanLifecycleService = scanLifecycleService;
    }

    /**
     * The concept of a RouteBundle is introduced to make chaining routes easier
     * <p>
     * Each RouteBundle contains a routeId and scanType for a route to be executed,
     * plus a list of destinations and recipients.
     * <p>
     * A destination is another route that is called after the route described by the routeId
     * and scanId is completed. Destination routes cannot be chained.
     * <p>
     * A recipient is another RouteBundle and is the mechanism by which routes are chained
     * together into a scan.
     * <p>
     * The following code is an example of how 3 routes can be chained together for a single scan.
     * <p>
     * RouteBundle fileWriterDestination = RouteBundle.builder()
     * .destinations(List.of())
     * .scanType("")
     * .routeId("seda:dataCollectionFileWrite")
     * .recipients(List.of())
     * .messagingServiceId(myMessagingService.getId())
     * .firstRouteInChain(false)
     * .build();
     * <p>
     * RouteBundle loggingDestination = RouteBundle.builder()
     * .destinations(List.of())
     * .scanType("")
     * .routeId("log:test")
     * .recipients(List.of())
     * .messagingServiceId(myMessagingService.getId())
     * .firstRouteInChain(false)
     * .build();
     * <p>
     * RouteBundle solaceSubscriptionConfiguration = RouteBundle.builder()
     * .destinations(List.of(fileWriterDestination))
     * .scanType("subscriptionConfiguration")
     * .routeId("solaceSubscriptionConfiguration")
     * .recipients(List.of())
     * .messagingServiceId(myMessagingService.getId())
     * .firstRouteInChain(false)
     * .build();
     * <p>
     * RouteBundle solaceQueueListing = RouteBundle.builder()
     * .destinations(List.of(loggingDestination))
     * .scanType("queueListing")
     * .routeId("solaceDataPublisher")
     * .recipients(List.of(solaceSubscriptionConfiguration))
     * .messagingServiceId(myMessagingService.getId())
     * .firstRouteInChain(true)
     * .build();
     * <p>
     * RouteBundle solaceQueueConfiguration = RouteBundle.builder()
     * .destinations(List.of(fileWriterDestination))
     * .scanType("queueConfiguration")
     * .routeId("solaceQueueConfiguration")
     * .recipients(List.of())
     * .messagingServiceId(myMessagingService.getId())
     * .firstRouteInChain(true)
     * .build();
     *
     * @param routeBundles - see description above
     * @return The id of the scan.
     */
    public String singleScan(List<RouteBundle> routeBundles, int numExpectedCompletionMessages, String groupId, String scanId) {

        ScanEntity savedScanEntity = null;

        for (RouteBundle routeBundle : routeBundles) {
            RouteEntity route = routeService.findById(routeBundle.getRouteId())
                    .orElseThrow();

            ScanEntity returnedScanEntity = setupScan(route, routeBundle, savedScanEntity, scanId);

            scanAsync(groupId, scanId, route, routeBundle.getMessagingServiceId());
            savedScanEntity = returnedScanEntity;
        }

        if (savedScanEntity != null) {
            ScanLifecycleEntity scannedLifecycleEntity = ScanLifecycleEntity.builder()
                    .scanId(scanId)
                    .numExpectedCompletionMessages(numExpectedCompletionMessages)
                    .build();

            scanLifecycleService.addScanLifecycleEntity(scannedLifecycleEntity);
            return scanId;
        }
        log.error("Unable to process scan request");
        return null;
    }

    @Transactional
    protected ScanEntity setupScan(RouteEntity route, RouteBundle routeBundle, ScanEntity scanEntity, String scanId) {
        ScanEntity savedScanEntity = saveScanEntity(route, routeBundle, scanEntity, scanId);

        List<ScanDestinationEntity> destinationEntities = routeBundle.getDestinations().stream()
                .map(destination -> ScanDestinationEntity.builder()
                        .scan(savedScanEntity)
                        .route(route)
                        .destination(destination.getRouteId())
                        .build())
                .collect(Collectors.toUnmodifiableList());

        if (!destinationEntities.isEmpty()) {
            scanRouteService.saveDestinations(destinationEntities);
        }

        List<ScanRecipientEntity> recipientEntities = routeBundle.getRecipients().stream()
                .map(recipient -> ScanRecipientEntity.builder()
                        .scan(savedScanEntity)
                        .route(route)
                        .recipient("seda:" + recipient.getRouteId())
                        .build())
                .collect(Collectors.toUnmodifiableList());

        if (!recipientEntities.isEmpty()) {
            scanRouteService.saveRecipients(recipientEntities);
        }

        setupRecipientsForScan(savedScanEntity, routeBundle, scanId);

        return savedScanEntity;
    }

    private ScanEntity saveScanEntity(RouteEntity route, RouteBundle routeBundle, ScanEntity scanEntity, String scanId) {
        ScanEntity returnScanEntity = scanEntity;
        if (returnScanEntity == null) {
            returnScanEntity = save(ScanEntity.builder()
                    .id(scanId)
                    .route(List.of(route))
                    .active(true)
                    .scanType(routeBundle.getScanType())
                    .build());
        } else {
            if (routeBundle.isFirstRouteInChain()) {
                ArrayList<RouteEntity> routeEntities = new ArrayList<>(scanEntity.getRoute());
                routeEntities.add(route);
                scanEntity.setRoute(routeEntities);
                save(scanEntity);
            }
        }
        return returnScanEntity;
    }

    protected void setupRecipientsForScan(ScanEntity scanEntity, RouteBundle routeBundle, String scanId) {

        for (RouteBundle recipient : routeBundle.getRecipients()) {
            RouteEntity route = routeService.findById(recipient.getRouteId())
                    .orElseThrow();
            setupScan(route, recipient, scanEntity, scanId);
        }
    }


    /**
     * Attempts to initiate a single "one time" scan. This scan does NOT get repeated.
     *
     * @param destinations       A list of Destinations to send the scan results to.
     * @param messagingServiceId The ID of the Messaging Service that is being scanned.
     * @param routeId            The ID of the Route.
     * @param scanType           The Type of Scan being executed.
     * @return The ID of this scan.
     * @throws Exception Any exception that could be thrown.
     */
    @Deprecated
    public String singleScan(List<String> destinations, List<String> recipients, String messagingServiceId,
                             String routeId, String scanType) {
        RouteEntity route = routeService.findById(routeId)
                .orElseThrow();

        String groupId = UUID.randomUUID().toString();

        ScanEntity savedScanEntity = setupScan(route, destinations, recipients, scanType);


        scanAsync(groupId, savedScanEntity.getId(), route, messagingServiceId)
                .whenComplete((exchange, exception) -> findById(savedScanEntity.getId())
                        .ifPresent(scanEntity -> {
                            scanEntity.setActive(false);

                            save(scanEntity);
                        }));

        return savedScanEntity.getId();
    }

    @Transactional
    protected ScanEntity setupScan(RouteEntity route, List<String> destinations, List<String> recipients,
                                   String scanType) {
        ScanEntity savedScanEntity = save(ScanEntity.builder()
                .route(List.of(route))
                .active(true)
                .scanType(scanType)
                .build());

        List<ScanDestinationEntity> destinationEntities = destinations.stream()
                .map(destination -> ScanDestinationEntity.builder()
                        .scan(savedScanEntity)
                        .route(route)
                        .destination(destination)
                        .build())
                .collect(Collectors.toUnmodifiableList());

        if (!destinationEntities.isEmpty()) {
            scanRouteService.saveDestinations(destinationEntities);
        }

        List<ScanRecipientEntity> recipientEntities = recipients.stream()
                .map(recipient -> ScanRecipientEntity.builder()
                        .scan(savedScanEntity)
                        .route(route)
                        .recipient(recipient)
                        .build())
                .collect(Collectors.toUnmodifiableList());

        if (!recipientEntities.isEmpty()) {
            scanRouteService.saveRecipients(recipientEntities);
        }

        setupConfigScan(savedScanEntity, destinations, recipients);

        return savedScanEntity;
    }

    protected void setupConfigScan(ScanEntity scanEntity, List<String> destinations, List<String> recipients) {

        for (String recipient : recipients) {
            String recipientPath = recipient.split(":")[1];
            RouteEntity route = routeService.findById(recipientPath)
                    .orElseThrow();

            List<ScanDestinationEntity> destinationEntities = destinations.stream()
                    .map(destination -> ScanDestinationEntity.builder()
                            .scan(scanEntity)
                            .route(route)
                            .destination(destination)
                            .build())
                    .collect(Collectors.toUnmodifiableList());

            if (!destinationEntities.isEmpty()) {
                scanRouteService.saveDestinations(destinationEntities);
            }
        }
    }

    public Optional<ScanEntity> findById(String scanId) {
        return repository.findById(scanId);
    }

    protected Exchange scan(String groupId, String scanId, RouteEntity route,
                            String messagingServiceId) {
        return producerTemplate.send("seda:" + route.getId(), exchange -> {
            // Need to set headers to let the Route have access to the Scan ID, Group ID, and Messaging Service ID.
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(MESSAGING_SERVICE_ID, messagingServiceId);
        });
    }

    /**
     * Initials an asynchronous Messaging Service scan.
     *
     * @param groupId            Not used at the moment. This will be the grouped Scan IDs.
     * @param scanId             The Scan ID to set.
     * @param route              Details about the Camel Route being invoked.
     * @param messagingServiceId The ID of the Messaging Service being scanned.
     * @return A Future of the Route result.
     */
    public CompletableFuture<Exchange> scanAsync(String groupId, String scanId, RouteEntity route,
                                                 String messagingServiceId) {
        return producerTemplate.asyncSend("seda:" + route.getId(), exchange -> {
            // Need to set headers to let the Route have access to the Scan ID, Group ID, and Messaging Service ID.
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(MESSAGING_SERVICE_ID, messagingServiceId);

            MDC.put(RouteConstants.SCAN_ID, scanId);
            MDC.put(RouteConstants.SCHEDULE_ID, groupId);
            MDC.put(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
        }).whenComplete((exchange, exception) -> {
            if (exception != null) {
                log.error("Exception occurred while executing route {} for scan {}.", route.getId(), scanId, exception);
            } else {
                log.debug("Successfully completed route {} for scan {}", route.getId(), scanId);
            }
            routeService.stopRoute(route);
        });
    }

    /**
     * Saves information pertaining to a Messaging Service scan.
     *
     * @param scanEntity The information of the Messaging Service scan.
     * @return The saved Messaging Service scan details.
     */
    protected ScanEntity save(ScanEntity scanEntity) {
        return repository.save(scanEntity);
    }
}

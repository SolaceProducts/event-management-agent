package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatusType;
import com.solace.maas.ep.event.management.agent.plugin.constants.SchedulerConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.SchedulerType;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanDestinationEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRepository;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCHEDULE_ID;

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

    public ScanService(ScanRepository repository, ScanRouteService scanRouteService,
                       RouteService routeService, ProducerTemplate producerTemplate) {
        this.repository = repository;
        this.scanRouteService = scanRouteService;
        this.routeService = routeService;
        this.producerTemplate = producerTemplate;
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
    public String singleScan(List<RouteBundle> routeBundles, String groupId, String scanId) {
        log.info("Scan request [{}]: Starting a single scan.", scanId);

        ScanEntity savedScanEntity = null;

        List<String> scanTypes = parseRouteBundle(routeBundles, new ArrayList<>());

        log.info("Scan request [{}]: Total of {} scan types to be retrieved: [{}]",
                scanId, scanTypes.size(), StringUtils.join(scanTypes, ", "));

        send(scanId, groupId, routeBundles.stream().findFirst().orElseThrow().getMessagingServiceId(),
                routeBundles.stream().findFirst().orElseThrow().getScanType(),
                ScanStatus.IN_PROGRESS, ScanStatusType.OVERALL, scanTypes);

        log.trace("RouteBundles to be processed: {}", routeBundles);

        String scanEntityId = Objects.requireNonNullElseGet(scanId, () -> UUID.randomUUID().toString());

        for (RouteBundle routeBundle : routeBundles) {
            log.trace("Processing RouteBundles: {}", routeBundle);

            RouteEntity route = routeService.findById(routeBundle.getRouteId())
                    .orElseThrow();

            ScanEntity returnedScanEntity = setupScan(route, routeBundle, savedScanEntity, scanEntityId);

            scanAsync(groupId, scanEntityId, route, routeBundle.getMessagingServiceId());
            savedScanEntity = returnedScanEntity;
        }

        if (savedScanEntity != null) {
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
     * Sends the initial scan  status message to signal the start of Messaging Service scan.
     *
     * @param scanId             The Scan ID to set.
     * @param groupId            Not used at the moment. This will be the grouped Scan IDs.
     * @param messagingServiceId The ID of the Messaging Service being scanned.
     * @param scanType           The type of scan.
     * @param status             The status of scan.
     * @param statusType         The type of scan status. Either the overall scan status, or the route status.
     * @param scanTypes          The list of scan types included in the scan request.
     */
    public void send(String scanId, String groupId, String messagingServiceId, String scanType,
                     ScanStatus status, ScanStatusType statusType, List<String> scanTypes) {
        producerTemplate.send("seda:scanStatusPublisher", exchange -> {
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, status);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, statusType);

            exchange.getIn().setBody(scanTypes);
        });
    }

    protected CompletableFuture<Exchange> scanAsync(String groupId, String scanId, RouteEntity route, String messagingServiceId) {
        return producerTemplate.asyncSend("seda:" + route.getId(), exchange -> {
            // Need to set headers to let the Route have access to the Scan ID, Group ID, and Messaging Service ID.
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(MESSAGING_SERVICE_ID, messagingServiceId);

            exchange.getIn().setHeader(SchedulerConstants.SCHEDULER_TERMINATION_TIMER, true);
            exchange.getIn().setHeader(SchedulerConstants.SCHEDULER_TYPE, SchedulerType.INTERVAL.name());
            exchange.getIn().setHeader(SchedulerConstants.SCHEDULER_DESTINATION, "seda:terminateAsyncProcess");
            exchange.getIn().setHeader(SchedulerConstants.SCHEDULER_START_DELAY, 5000);
            exchange.getIn().setHeader(SchedulerConstants.SCHEDULER_INTERVAL, 5000);
            exchange.getIn().setHeader(SchedulerConstants.SCHEDULER_REPEAT_COUNT, 0);

            MDC.put(RouteConstants.SCAN_ID, scanId);
            MDC.put(RouteConstants.SCHEDULE_ID, groupId);
            MDC.put(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
        }).whenComplete((exchange, exception) -> {
            if (exception != null) {
                log.error("Exception occurred while executing route {} for scan request: {}.", route.getId(), scanId, exception);
            } else {
                log.trace("Camel route {} for scan request: {} has been executed successfully.", route.getId(), scanId);
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

    protected List<String> parseRouteBundle(List<RouteBundle> routeBundles, List<String> scanTypes) {
        for (RouteBundle routeBundle : routeBundles) {
            scanTypes.add(routeBundle.getScanType());
            if (routeBundle.getRecipients() != null && !routeBundle.getRecipients().isEmpty()) {
                parseRouteBundle(routeBundle.getRecipients(), scanTypes);
            }
        }
        return scanTypes;
    }
}

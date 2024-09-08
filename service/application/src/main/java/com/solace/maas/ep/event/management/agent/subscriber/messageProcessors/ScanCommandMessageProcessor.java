package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanCommandMessageProcessor implements MessageProcessor<ScanCommandMessage> {

    private static final String DEFAULT_DESTINATION = "FILE_WRITER";
    private final ScanManager scanManager;
    private final DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper;

    public ScanCommandMessageProcessor(ScanManager scanManager,
                                       DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper) {
        this.scanManager = scanManager;
        this.dynamicResourceConfigurationHelper = dynamicResourceConfigurationHelper;
    }

    @Override
    public void processMessage(ScanCommandMessage message) {
        MDC.clear();

        List<String> destinations = new ArrayList<>();
        List<String> entityTypes = new ArrayList<>();

        log.debug("Received scan command message: {} for event broker: {}, traceId: {}",
                message, message.getMessagingServiceId(), message.getTraceId());

        if (CollectionUtils.isNotEmpty(message.getResources())) {
            dynamicResourceConfigurationHelper.loadSolaceBrokerResourceConfigurations(message.getResources());
        }

        message.getScanTypes().forEach(scanType -> entityTypes.add(scanType.name()));

        if (message.getDestinations() == null) {
            destinations.add(DEFAULT_DESTINATION);
        } else {
            message.getDestinations().forEach(destination -> destinations.add(destination.name()));
            if (!destinations.contains(DEFAULT_DESTINATION)) {
                destinations.add(DEFAULT_DESTINATION);
            }
        }

        List<String> scanRequestDestinations = destinations.stream()
                .distinct().toList();

        ScanRequestBO scanRequestBO = ScanRequestBO.builder()
                .messagingServiceId(message.getMessagingServiceId())
                .scanId(!StringUtils.isEmpty(message.getScanId()) ? message.getScanId() : UUID.randomUUID().toString())
                .traceId(message.getTraceId())
                .actorId(message.getActorId())
                .scanTypes(entityTypes)
                .destinations(scanRequestDestinations)
                .build();

        log.info("Received scan request {}. Request details: {}", scanRequestBO.getScanId(), scanRequestBO);

        String scanId = scanManager.scan(scanRequestBO);
        waitForScanCompletion(scanId);
    }

    public void waitForScanCompletion(String scanId) {
        try {
            await()
                    .atMost(5, MINUTES)
                    .pollInterval(10, SECONDS)
                    .pollInSameThread()
                    .atMost(5, MINUTES)
                    .until(() -> {
                        try {
                            return waitUntilScanIsCompleted(scanId);
                        } catch (Exception e) {
                            System.out.println("Exception occurred: " + e.getMessage() + ". Retrying...");
                            return false;
                        }
                    });
        } catch (ConditionTimeoutException e) {
            System.out.println("Scan did not complete within 5 minutes or after 5 retries.");
            // Handle the timeout scenario as needed
        }
    }

    private boolean waitUntilScanIsCompleted(String scanId) {
        boolean res = scanManager.isScanComplete(scanId);
        if (res) {
            log.info("Scan with id {} completed successfully. Will remove from queue now", scanId);
            try {
                MINUTES.sleep(1);
            } catch (InterruptedException e) {
                log.error("Error occurred while waiting for scan to complete: {}", e.getMessage());
            }
        }
        return res;
    }

    @Override
    public Class supportedClass() {
        return ScanCommandMessage.class;
    }

    @Override
    public ScanCommandMessage castToMessageClass(Object message) {
        return (ScanCommandMessage) message;
    }

    @Override
    public void onFailure(Exception e, ScanCommandMessage message) {
        scanManager.handleError(e, message);
    }
}

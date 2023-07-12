package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanCommandMessageHandler extends SolaceMessageHandler<ScanCommandMessage> {

    private static final String DEFAULT_DESTINATION = "FILE_WRITER";
    private final ScanManager scanManager;

    public ScanCommandMessageHandler(SolaceConfiguration solaceConfiguration, SolaceSubscriber solaceSubscriber, ScanManager scanManager) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/v1/scanStart/>", solaceSubscriber);
        this.scanManager = scanManager;
    }

    @Override
    public void receiveMessage(String destinationName, ScanCommandMessage message) {
        MDC.clear();

        List<String> destinations = new ArrayList<>();
        List<String> entityTypes = new ArrayList<>();

        log.debug("Received scan command message: {} for event broker: {}, traceId: {}",
                message, message.getMessagingServiceId(), message.getTraceId());

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
                .distinct()
                .collect(Collectors.toUnmodifiableList());

        ScanRequestBO scanRequestBO = ScanRequestBO.builder()
                .messagingServiceId(message.getMessagingServiceId())
                .scanId(!StringUtils.isEmpty(message.getScanId()) ? message.getScanId() : UUID.randomUUID().toString())
                .traceId(message.getTraceId())
                .scanTypes(entityTypes)
                .destinations(scanRequestDestinations)
                .build();

        log.info("Received scan request {}. Request details: {}", scanRequestBO.getScanId(), scanRequestBO);

        scanManager.scan(scanRequestBO);
    }
}

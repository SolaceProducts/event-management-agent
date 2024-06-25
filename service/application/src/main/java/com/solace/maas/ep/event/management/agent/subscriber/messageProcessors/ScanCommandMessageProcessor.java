package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        scanManager.scan(scanRequestBO);
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
        log.debug("Requires implementation");
    }
}

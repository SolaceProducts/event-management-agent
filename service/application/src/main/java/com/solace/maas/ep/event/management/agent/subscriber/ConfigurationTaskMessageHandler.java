package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ConfigurationTaskMessage;
import com.solace.maas.ep.common.model.ConfigDestination;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.configurationTaskManager.ConfigurationTaskManager;
import com.solace.maas.ep.event.management.agent.configurationTaskManager.model.ConfigurationTaskBO;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ExcludeFromJacocoGeneratedReport
@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ConfigurationTaskMessageHandler  extends SolaceMessageHandler<ConfigurationTaskMessage>{

    private static final String DEFAULT_DESTINATION = ConfigDestination.BROKER.name();
    private static final String TOPIC_SUFFIX = "config/v1/request/>";

    private final ConfigurationTaskManager configurationTaskManager;

    public ConfigurationTaskMessageHandler(SolaceConfiguration solaceConfiguration,
                                           SolaceSubscriber solaceSubscriber, ConfigurationTaskManager configurationTaskManager) {
        super(solaceConfiguration.getTopicPrefix() + TOPIC_SUFFIX, solaceSubscriber);
        this.configurationTaskManager = configurationTaskManager;
    }

    @Override
    public void receiveMessage(String destinationName, ConfigurationTaskMessage messageIn) {
        MDC.clear();
        ConfigurationTaskMessage<?> message = messageIn;
        log.debug("Received config message: {} for messaging service: {}",
                message, message.getMessagingServiceId());
        List<String> destinations = new ArrayList<>();
        if (message.getDestinations() != null) {
            message.getDestinations().forEach(destination -> destinations.add(destination.name()));
        }
        if (!destinations.contains(DEFAULT_DESTINATION)) {
            destinations.add(DEFAULT_DESTINATION);
        }
        List<String> configDestinations = destinations.stream()
                .distinct()
                .collect(Collectors.toUnmodifiableList());
        ConfigurationTaskBO configurationTaskBO = ConfigurationTaskBO.builder()
                .id(!StringUtils.isEmpty(message.getTaskId()) ? message.getTaskId() : UUID.randomUUID().toString())
                .messagingServiceId(message.getMessagingServiceId())
                .configType(message.getConfigType())
                .taskConfigs(Collections.unmodifiableList(message.getTaskConfigs()))
                .destinations(configDestinations)
                .build();
        log.info("Received scan request {}. Request details: {}", configurationTaskBO.getConfigType(), configurationTaskBO.getId());
        boolean status = this.configurationTaskManager.execute(configurationTaskBO);
    }
}

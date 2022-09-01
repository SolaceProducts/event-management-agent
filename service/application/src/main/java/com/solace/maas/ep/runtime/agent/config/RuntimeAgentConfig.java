package com.solace.maas.ep.runtime.agent.config;

import com.solace.maas.ep.runtime.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.runtime.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.runtime.agent.plugin.config.ClientConnectionDetails;
import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.MessagingServiceProperties;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.runtime.agent.plugin.properties.KafkaProperties;
import com.solace.maas.ep.runtime.agent.plugin.properties.SolacePluginProperties;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.runtime.agent.service.MessagingServiceDelegateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;


@ExcludeFromJacocoGeneratedReport
@Slf4j
@Configuration
@Profile("!TEST")
public class RuntimeAgentConfig implements ApplicationRunner {

    private final KafkaProperties kafkaProperties;
    private final SolacePluginProperties solaceProperties;
    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;
    private final ClientConnectionDetails clientConnectionDetails;

    @Autowired
    public RuntimeAgentConfig(KafkaProperties kafkaProperties, SolacePluginProperties solaceProperties,
                              MessagingServiceDelegateServiceImpl messagingServiceDelegateService,
                              EventPortalProperties eventPortalProperties,
                              ClientConnectionDetails clientConnectionDetails) {
        this.kafkaProperties = kafkaProperties;
        this.solaceProperties = solaceProperties;
        this.messagingServiceDelegateService = messagingServiceDelegateService;
        this.clientConnectionDetails = clientConnectionDetails;
    }

    @Override
    public void run(ApplicationArguments args) {
        createKafkaMessagingServices();
        createSolaceMessagingServices();
    }

    private void createSolaceMessagingServices() {
        List<MessagingServiceProperties> solaceMessagingServices = solaceProperties.getMessagingServices();

        if (solaceMessagingServices != null) {
            solaceMessagingServices.forEach(solaceMessagingService -> {
                List<ConnectionDetailsEvent> connectionDetails = new ArrayList<>();

                solaceMessagingService.getManagement().getConnections()
                        .forEach(solaceMessagingServiceConnection -> {
                            ConnectionDetailsEvent connectionDetailsEvent = clientConnectionDetails.createConnectionDetails(
                                    solaceMessagingService.getId(), solaceMessagingServiceConnection, MessagingServiceType.SOLACE);

                            connectionDetails.add(connectionDetailsEvent);
                        });

                MessagingServiceEvent messagingServiceEvent = MessagingServiceEvent.builder()
                        .id(solaceMessagingService.getId())
                        .name(solaceMessagingService.getName())
                        .messagingServiceType(MessagingServiceType.SOLACE)
                        .connectionDetails(connectionDetails)
                        .build();

                MessagingServiceEntity addedMessagingServiceEntity =
                        messagingServiceDelegateService.addMessagingService(messagingServiceEvent);

                log.info("Created Solace messaging service: {} {}", solaceMessagingService.getId(), addedMessagingServiceEntity.getName());
            });
        } else {
            log.info("Could not find Solace messaging services.");
        }
    }

    private void createKafkaMessagingServices() {
        List<MessagingServiceProperties> kafkaMessagingServices = kafkaProperties.getMessagingServices();

        if (kafkaMessagingServices != null) {
            kafkaMessagingServices.forEach(kafkaMessagingService -> {
                List<ConnectionDetailsEvent> connectionDetails = new ArrayList<>();

                kafkaMessagingService.getManagement().getConnections()
                        .forEach(kafkaMessagingServiceConnection -> {
                            ConnectionDetailsEvent connectionDetailsEvent =
                                    clientConnectionDetails.createConnectionDetails(
                                            kafkaMessagingService.getId(), kafkaMessagingServiceConnection, MessagingServiceType.KAFKA);

                            connectionDetails.add(connectionDetailsEvent);
                        });

                MessagingServiceEvent messagingServiceEvent = MessagingServiceEvent.builder()
                        .id(kafkaMessagingService.getId())
                        .name(kafkaMessagingService.getName())
                        .messagingServiceType(MessagingServiceType.KAFKA)
                        .connectionDetails(connectionDetails)
                        .build();

                MessagingServiceEntity addedMessagingServiceEntity =
                        messagingServiceDelegateService.addMessagingService(messagingServiceEvent);

                log.info("Created Kafka messaging service: {} {}", kafkaMessagingService.getId(), addedMessagingServiceEntity.getName());
            });
        } else {
            log.info("Could not find Kafka messaging services.");
        }
    }
}

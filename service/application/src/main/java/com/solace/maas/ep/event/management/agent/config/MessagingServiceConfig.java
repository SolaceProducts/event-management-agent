package com.solace.maas.ep.event.management.agent.config;

import com.solace.maas.ep.event.management.agent.config.plugin.ClientConnectionDetails;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceEntityToEventConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ExcludeFromJacocoGeneratedReport
@Data
@PropertySource("classpath:application.yml")
@Configuration
@ConfigurationProperties(prefix = "plugins")
@Slf4j
@Profile("!TEST")
public class MessagingServiceConfig implements ApplicationRunner {
    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;
    private final ClientConnectionDetails clientConnectionDetails;
    private List<MessagingServiceEntity> messagingServices;
    private final MessagingServiceEntityToEventConverter entityToEventConverter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (Objects.nonNull(messagingServices)) {
            log.info(
                    String.format("Creating messaging service(s): %s.",
                            messagingServices.stream()
                                    .map(MessagingServiceEntity::getName)
                                    .collect(Collectors.joining(", ")))
            );

            List<MessagingServiceEvent> messagingServiceEvents = messagingServices.stream()
                    .map(entityToEventConverter::convert)
                    .collect(Collectors.toUnmodifiableList());

            messagingServiceDelegateService.addMessagingServices(messagingServiceEvents)
                    .forEach(messagingServiceEntity ->
                            log.info("Created {} Messaging Service with id: {} and name: {}.",
                                    messagingServiceEntity.getType(),
                                    messagingServiceEntity.getId(), messagingServiceEntity.getName()));
        } else {
            log.info("No Messaging Service(s) created.");
        }
    }
}

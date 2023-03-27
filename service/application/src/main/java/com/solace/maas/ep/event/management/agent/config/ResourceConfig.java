package com.solace.maas.ep.event.management.agent.config;

import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.MessagingServicePluginPropertyToEventConverter;
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
public class ResourceConfig implements ApplicationRunner {
    private final MessagingServiceDelegateServiceImpl messagingServiceDelegateService;
    private List<MessagingServicePluginProperties> resources;
    private final MessagingServicePluginPropertyToEventConverter configToEventConverter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (Objects.nonNull(resources)) {
            log.info(
                    String.format("Creating messaging service(s): [%s].",
                            resources.stream()
                                    .map(MessagingServicePluginProperties::getName)
                                    .collect(Collectors.joining("],[")))
            );

            List<MessagingServiceEvent> messagingServiceEvents = resources.stream()
                    .map(configToEventConverter::convert)
                    .collect(Collectors.toUnmodifiableList());

            messagingServiceDelegateService.addMessagingServices(messagingServiceEvents)
                    .forEach(messagingServiceEntity ->
                            log.info("Created [{}] Messaging Service with id: [{}] and name: [{}].",
                                    messagingServiceEntity.getType(),
                                    messagingServiceEntity.getId(), messagingServiceEntity.getName()));

            messagingServiceDelegateService.addMessagingServicesRelations(resources);

        } else {
            log.info("No Messaging Service(s) created.");
        }
    }
}

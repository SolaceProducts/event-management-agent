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
    //    private List<MessagingServicePluginProperties> messagingServices;
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
//            messagingServiceDelegateService.addMessagingServiceEntities(messagingServices);
//
//            messagingServiceDelegateService.getMessagingServiceById(messagingServices.get(0).getId());

            List<MessagingServiceEvent> messagingServiceEvents = messagingServices.stream()
                    .map(entityToEventConverter::convert)
                    .collect(Collectors.toUnmodifiableList());
//            List<MessagingServiceEvent> messagingServiceEvents = messagingServices.stream()
//                    .map(messagingService -> {
//                        List<ConnectionDetailsEvent> connectionDetails = new ArrayList<>();

//                        messagingService.getConnections()
//                                .forEach(messagingServiceConnection -> {
////                                    ConnectionDetailsEvent connectionDetailsEvent = clientConnectionDetails.createConnectionDetails(
////                                            messagingService.getId(), messagingServiceConnection, MessagingServiceType.SOLACE);
//
//
//                                    List<AuthenticationDetailsEvent> authenticationDetailsEvents = messagingServiceConnection.getAuthentication().stream()
//                                            .map(authenticationDetailsEntity -> {
//                                                CredentialDetailsEntity credentialDetails = authenticationDetailsEntity
//                                                        .getCredentials().stream().findFirst().get();
//
//                                                return AuthenticationDetailsEvent.builder()
//                                                        .id(authenticationDetailsEntity.getId())
//                                                        .username(MessagingServiceConfigurationUtil.getUsername(credentialDetails))
//                                                        .password(MessagingServiceConfigurationUtil.getPassword(credentialDetails))
//                                                        .protocol(authenticationDetailsEntity.getProtocol())
//                                                        .authType(MessagingServiceConfigurationUtil.getAuthenticationType(authenticationDetailsEntity))
//                                                        .operations(credentialDetails.getOperations().stream()
//                                                                .map(op ->
//                                                                        AuthenticationOperationDetailsEvent.builder()
//                                                                                .name(op.getName())
//                                                                                .build())
//                                                                .collect(Collectors.toList()))
//                                                        .build();
//                                            }).collect(Collectors.toList());
//
//                                    ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
//                                            .name(messagingServiceConnection.getName())
//                                            .url(messagingServiceConnection.getUrl())
//                                            .msgVpn(MessagingServiceConfigurationUtil.getMsgVpn(messagingServiceConnection))
//                                            .sempPageSize(MessagingServiceConfigurationUtil.getSempPageSize(messagingServiceConnection))
//                                            .authenticationDetails(authenticationDetailsEvents)
//                                            .build();
//                                    connectionDetails.add(connectionDetailsEvent);
//                                });
//
//                        return MessagingServiceEvent.builder()
//                                .id(messagingService.getId())
//                                .name(messagingService.getName())
//                                .messagingServiceType(messagingService.getType())
//                                .connectionDetails(connectionDetails)
//                                .build();
//                    }).collect(Collectors.toUnmodifiableList());

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

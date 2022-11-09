package com.solace.maas.ep.event.management.agent.plugin.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Properties;

/**
 * The KafkaClientManagerImpl class is a temporary placeholder being used to create a Kafka AdminClient.
 * This class will be extended in the future to add more functionality.
 */
@Slf4j
@Data
public class KafkaClientManagerImpl implements MessagingServiceClientManager<AdminClient> {

    @Override
    public AdminClient getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.trace("Creating Kafka admin client for messaging service {}.", connectionDetailsEvent.getMessagingServiceId());

        try {
            Properties properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, connectionDetailsEvent.getUrl());
            properties.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10_000);
            properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);

            Optional<AuthenticationDetailsEvent> authenticationDetailsEvent =
                    connectionDetailsEvent.getAuthenticationDetails().stream().findFirst();
            authenticationDetailsEvent.ifPresent(auth -> {
                if (StringUtils.isNotBlank(auth.getProtocol())) {
                    properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, auth.getProtocol());
                }
                if (!CollectionUtils.isEmpty(auth.getCredentials())) {
                    CredentialDetailsEvent credentialDetailsEvent = auth.getCredentials().stream().findFirst().get();
                    if (!CollectionUtils.isEmpty(credentialDetailsEvent.getProperties())) {
                        credentialDetailsEvent.getProperties()
                                .forEach(prop -> properties.put(prop.getName(), prop.getValue()));
                    }
                }
            });

            AdminClient adminClient = AdminClient.create(properties);

            log.trace("Kafka admin client created for {}.", connectionDetailsEvent.getMessagingServiceId());
            return adminClient;
        } catch (KafkaException e) {
            log.error("Could not create Kafka admin client for messaging service {}. Error: {}, cause: {}",
                    connectionDetailsEvent.getMessagingServiceId(), e.getMessage(), String.valueOf(e.getCause()));
            throw e;
        }
    }
}

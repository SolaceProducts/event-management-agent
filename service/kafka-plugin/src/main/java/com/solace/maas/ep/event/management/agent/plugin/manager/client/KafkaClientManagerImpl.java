package com.solace.maas.ep.event.management.agent.plugin.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.exception.PluginClientException;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The KafkaClientManagerImpl class is a temporary placeholder being used to create a Kafka AdminClient.
 * This class will be extended in the future to add more functionality.
 */
@Slf4j
@Data
public class KafkaClientManagerImpl implements MessagingServiceClientManager<AdminClient> {
    private final int maxIdle;
    private final TimeUnit maxIdleTimeUnit;

    private final int requestTimeout;
    private final TimeUnit requestTimeoutTimeUnit;

    private final int reconnectBackoff;
    private final TimeUnit reconnectBackoffTimeUnit;

    private final int maxReconnectBackoff;
    private final TimeUnit maxReconnectBackoffTimeUnit;

    public KafkaClientManagerImpl(KafkaClientConfig kafkaClientConfig) {
        maxIdle = kafkaClientConfig.getConnections().getMaxIdle().getValue();
        maxIdleTimeUnit = kafkaClientConfig.getConnections().getMaxIdle().getUnit();

        requestTimeout = kafkaClientConfig.getConnections().getRequestTimeout().getValue();
        requestTimeoutTimeUnit = kafkaClientConfig.getConnections().getRequestTimeout().getUnit();

        reconnectBackoff = kafkaClientConfig.getReconnections().getBackoff().getValue();
        reconnectBackoffTimeUnit = kafkaClientConfig.getReconnections().getBackoff().getUnit();

        maxReconnectBackoff = kafkaClientConfig.getReconnections().getMaxBackoff().getValue();
        maxReconnectBackoffTimeUnit = kafkaClientConfig.getReconnections().getMaxBackoff().getUnit();
    }

    @Override
    public AdminClient getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.trace("Creating Kafka admin client for messaging service {}.", connectionDetailsEvent.getMessagingServiceId());

        try {
            Properties properties = buildProperties(connectionDetailsEvent);
            AdminClient adminClient = AdminClient.create(properties);

            log.trace("Kafka admin client created for {}.", connectionDetailsEvent.getMessagingServiceId());
            return adminClient;
        } catch (KafkaException e) {
            log.error("Could not create Kafka admin client for messaging service {}. Error: {}, cause: {}",
                    connectionDetailsEvent.getMessagingServiceId(), e.getMessage(), String.valueOf(e.getCause()));
            throw new PluginClientException(e.getMessage(), e);
        }
    }

    public Properties buildProperties(ConnectionDetailsEvent connectionDetailsEvent) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, connectionDetailsEvent.getUrl());
        properties.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, maxIdle);
        properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        properties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, reconnectBackoff);
        properties.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, maxReconnectBackoff);

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
                    log.debug("The kafka admin client '{}' has the following credential properties: [{}]",
                            connectionDetailsEvent.getMessagingServiceId(),
                            credentialDetailsEvent.getProperties().stream()
                                    .map(EventProperty::getName)
                                    .collect(Collectors.joining(","))
                    );
                }
            }
        });
        return properties;
    }
}

package com.solace.maas.ep.event.management.agent.plugin.kafka.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.util.MessagingServiceConfigurationUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.config.SslConfigs;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

import static com.solace.maas.ep.event.management.agent.plugin.util.MessagingServiceConfigurationUtil.getProperty;

/**
 * The KafkaClientManagerImpl class is a temporary placeholder being used to create a Kafka AdminClient.
 * This class will be extended in the future to add more functionality.
 */
@Slf4j
@Data
public class KafkaClientManagerImpl implements MessagingServiceClientManager<AdminClient> {

    @Override
    public AdminClient getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.info("Creating Kafka admin client for messaging service {}.", connectionDetailsEvent.getMessagingServiceId());

        try {
            Properties properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, connectionDetailsEvent.getUrl());
            properties.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10_000);
            properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);

            Optional<AuthenticationDetailsEvent> authenticationDetailsEvent =
                    connectionDetailsEvent.getAuthenticationDetails().stream().findFirst();
            authenticationDetailsEvent.ifPresent(auth -> {
                String authType = MessagingServiceConfigurationUtil.getAuthenticationType(auth);
                if ("mtls".equals(authType)) {
                    populateMTLSProperties(auth, properties, connectionDetailsEvent.getMessagingServiceId());
                }
            });

            AdminClient adminClient = AdminClient.create(properties);

            log.info("Kafka admin client created for {}.", connectionDetailsEvent.getMessagingServiceId());
            return adminClient;
        } catch (KafkaException e) {
            log.error("Could not create Kafka admin client for messaging service {}. Error: {}, cause: {}",
                    connectionDetailsEvent.getMessagingServiceId(), e.getMessage(), String.valueOf(e.getCause()));
            throw e;
        }
    }

    private void populateMTLSProperties(AuthenticationDetailsEvent authenticationDetailsEvent,
                                        Properties properties, String messagingServiceId) {
        CredentialDetailsEvent credentialDetailsEvent = authenticationDetailsEvent.getCredentials().stream()
                .findFirst().orElseThrow(() -> new NoSuchElementException(
                        String.format("No credentials configured for authentication details for service with id %s",
                                messagingServiceId)));

        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");

        addProperty(credentialDetailsEvent, properties, "truststoreLocation",
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, messagingServiceId);
        addProperty(credentialDetailsEvent, properties, "truststorePassword",
                SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, messagingServiceId);
        addProperty(credentialDetailsEvent, properties, "keystoreLocation",
                SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, messagingServiceId);
        addProperty(credentialDetailsEvent, properties, "keystorePassword",
                SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, messagingServiceId);
        addProperty(credentialDetailsEvent, properties, "keyPassword",
                SslConfigs.SSL_KEY_PASSWORD_CONFIG, messagingServiceId);
    }

    private void addProperty(CredentialDetailsEvent credentialDetailsEvent, Properties properties,
                             String credentialKey, String propertyKey, String messagingServiceId) {
        String value = getProperty(credentialDetailsEvent.getProperties(), credentialKey);
        if (StringUtils.isEmpty(value)) {
            throw new NoSuchElementException(
                    String.format("No '%s' configured in the credential properties for service with id %s",
                            credentialKey, messagingServiceId));
        }
        properties.put(propertyKey, value);
    }
}

package com.solace.maas.ep.event.management.agent.plugin.kafka.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.KafkaException;

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
        log.info("Creating Kafka admin client for messaging service {}.", connectionDetailsEvent.getMessagingServiceId());

        try {
            Properties properties = new Properties();
            properties.put("bootstrap.servers", connectionDetailsEvent.getConnectionUrl());
            properties.put("connections.max.idle.ms", 10_000);
            properties.put("request.timeout.ms", 5000);

            AdminClient adminClient = AdminClient.create(properties);

            log.info("Kafka admin client created for {}.", connectionDetailsEvent.getMessagingServiceId());
            return adminClient;
        } catch (KafkaException e) {
            log.error("Could not create Kafka admin client for messaging service {}. Error: {}, cause: {}",
                    connectionDetailsEvent.getMessagingServiceId(), e.getMessage(), String.valueOf(e.getCause()));
            throw e;
        }
    }
}

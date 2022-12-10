package com.solace.maas.ep.event.management.agent.plugin.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.config.SslConfigs;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaClientManagerTests {

    private final KafkaClientManagerImpl kafkaClientManager = new KafkaClientManagerImpl();

    @Test
    public void testProperties() {
        ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                .url("connection_url")
                .messagingServiceId("messaging_service_id")
                .name("conn_name")
                .authenticationDetails(List.of(AuthenticationDetailsEvent.builder()
                        .protocol("SSL")
                        .credentials(List.of(CredentialDetailsEvent.builder()
                                .properties(List.of(
                                        buildProperty("ssl.truststore.location", "/trust/location/truststore.jks"),
                                        buildProperty("ssl.truststore.password", "trustpass"),
                                        buildProperty("ssl.keystore.password", "keypass"),
                                        buildProperty("ssl.keystore.location", "/trust/location/keystore.jks"),
                                        buildProperty("ssl.key.password", "keyPass")
                                )).build()))
                        .build()))
                .build();

        Properties properties = kafkaClientManager.buildProperties(connectionDetailsEvent);
        assertThat(properties.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG)).isEqualTo("connection_url");
        assertThat(properties.get(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG)).isEqualTo(10000);
        assertThat(properties.get(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG)).isEqualTo(5000);
        assertThat(properties.get(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG)).isEqualTo("SSL");
        assertThat(properties.get(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG)).isEqualTo("/trust/location/truststore.jks");
        assertThat(properties.get(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG)).isEqualTo("trustpass");
        assertThat(properties.get(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG)).isEqualTo("keypass");
        assertThat(properties.get(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG)).isEqualTo("/trust/location/keystore.jks");
        assertThat(properties.get(SslConfigs.SSL_KEY_PASSWORD_CONFIG)).isEqualTo("keyPass");
    }

    @Test
    public void adminClientCreatePassThroughCredentialProperties() {
        ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                .url("localhost:12345")
                .messagingServiceId("messaging_service_id")
                .name("conn_name")
                .authenticationDetails(List.of(AuthenticationDetailsEvent.builder()
                        .protocol("PLAINTEXT")
                        .credentials(List.of(CredentialDetailsEvent.builder()
                                .properties(List.of(
                                        buildProperty(CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG, "1234")
                                )).build()))
                        .build()))
                .build();

        AdminClient adminClient = kafkaClientManager.getClient(connectionDetailsEvent);

        KafkaAdminClient kafkaAdminClient = (KafkaAdminClient) adminClient;
        int requestTimeout = 0;
        try {
            Field requestTimeoutField = kafkaAdminClient.getClass().getDeclaredField("requestTimeoutMs");
            requestTimeoutField.setAccessible(true);
            requestTimeout = requestTimeoutField.getInt(kafkaAdminClient);

        } catch (NoSuchFieldException | SecurityException e) {
            fail("Hit exception getting field access");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("could not get request timeout");
        }
        assertThat(requestTimeout).isEqualTo(1234);
    }

    @Test
    public void adminClientCreateFailure() {
        ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                .url("connection_url")
                .messagingServiceId("messaging_service_id")
                .name("conn_name")
                .authenticationDetails(List.of(AuthenticationDetailsEvent.builder()
                        .protocol("SSL")
                        .credentials(List.of(CredentialDetailsEvent.builder()
                                .properties(List.of(
                                        buildProperty("ssl.truststore.location", "/trust/location/truststore.jks"),
                                        buildProperty("ssl.truststore.password", "trustpass"),
                                        buildProperty("ssl.keystore.password", "keypass"),
                                        buildProperty("ssl.keystore.location", "/trust/location/keystore.jks"),
                                        buildProperty("ssl.key.password", "keyPass")
                                )).build()))
                        .build()))
                .build();

        Exception exception = assertThrows(KafkaException.class, () -> {
            kafkaClientManager.getClient(connectionDetailsEvent);
        });
        assertTrue(exception.getCause().getMessage().contains("Invalid url"));
    }

    private EventProperty buildProperty(String name, String value) {
        return EventProperty.builder()
                .name(name)
                .value(value)
                .build();
    }
}

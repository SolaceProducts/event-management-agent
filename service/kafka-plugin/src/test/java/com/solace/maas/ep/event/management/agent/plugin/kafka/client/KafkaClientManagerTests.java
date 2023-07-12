package com.solace.maas.ep.event.management.agent.plugin.kafka.client;

import com.solace.maas.ep.event.management.agent.plugin.KafkaTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.KafkaClientManagerImpl;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import lombok.SneakyThrows;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.config.SslConfigs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KafkaTestConfig.class)
class KafkaClientManagerTests {

    @Mock
    KafkaClientConfig kafkaClientConfig;

    @BeforeEach
    void setupMocks() {
        KafkaClientConnection kafkaClientConnection = mock(KafkaClientConnection.class);
        KafkaClientReconnection kafkaClientReconnection = mock(KafkaClientReconnection.class);

        KafkaClientConnectionConfig kafkaClientConnectionConfigTimeout = mock(KafkaClientConnectionConfig.class);
        KafkaClientConnectionConfig kafkaClientConnectionConfigMaxIdle = mock(KafkaClientConnectionConfig.class);
        KafkaClientConnectionConfig kafkaClientConnectionConfigRequestTimeout = mock(KafkaClientConnectionConfig.class);

        KafkaClientReconnectionConfig kafkaClientReconnectionConfigBackoff = mock(KafkaClientReconnectionConfig.class);
        KafkaClientReconnectionConfig kafkaClientReconnectionConfigBackoffMax = mock(KafkaClientReconnectionConfig.class);

        when(kafkaClientConfig.getConnections()).thenReturn(kafkaClientConnection);
        when(kafkaClientConfig.getReconnections()).thenReturn(kafkaClientReconnection);

        when(kafkaClientConnection.getTimeout()).thenReturn(kafkaClientConnectionConfigTimeout);
        when(kafkaClientConnectionConfigTimeout.getValue()).thenReturn(60_000);
        when(kafkaClientConnectionConfigTimeout.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientConnection.getMaxIdle()).thenReturn(kafkaClientConnectionConfigMaxIdle);
        when(kafkaClientConnectionConfigMaxIdle.getValue()).thenReturn(10_000);
        when(kafkaClientConnectionConfigMaxIdle.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientConnection.getRequestTimeout()).thenReturn(kafkaClientConnectionConfigRequestTimeout);
        when(kafkaClientConnectionConfigRequestTimeout.getValue()).thenReturn(5_000);
        when(kafkaClientConnectionConfigRequestTimeout.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientReconnection.getBackoff()).thenReturn(kafkaClientReconnectionConfigBackoff);
        when(kafkaClientReconnectionConfigBackoff.getValue()).thenReturn(50);
        when(kafkaClientReconnectionConfigBackoff.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientReconnection.getMaxBackoff()).thenReturn(kafkaClientReconnectionConfigBackoffMax);
        when(kafkaClientReconnectionConfigBackoffMax.getValue()).thenReturn(1000);
        when(kafkaClientReconnectionConfigBackoffMax.getUnit()).thenReturn(TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    @Test
    void testProperties() {
        KafkaClientManagerImpl kafkaClientManager = new KafkaClientManagerImpl(kafkaClientConfig);

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
        
        assertThat(properties)
                .containsEntry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "connection_url")
                .containsEntry(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000)
                .containsEntry(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000)
                .containsEntry(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL")
                .containsEntry(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/trust/location/truststore.jks")
                .containsEntry(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "trustpass")
                .containsEntry(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "keypass")
                .containsEntry(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/trust/location/keystore.jks")
                .containsEntry(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "keyPass");
    }

    @Test
    void adminClientCreatePassThroughCredentialProperties() {
        KafkaClientManagerImpl kafkaClientManager = new KafkaClientManagerImpl(kafkaClientConfig);

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
    void adminClientCreateFailure() {
        KafkaClientManagerImpl kafkaClientManager = new KafkaClientManagerImpl(kafkaClientConfig);

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

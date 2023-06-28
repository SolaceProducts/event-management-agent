package com.solace.maas.ep.event.management.agent.plugin;

import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestConfiguration
public class KafkaTestConfig {
    @Bean
    @Primary
    public MessagingServiceDelegateService messagingServiceDelegateService() {
        return mock(MessagingServiceDelegateService.class);
    }

    @Bean
    @Primary
    public KafkaClientConfig kafkaClientConfig() {
        KafkaClientConfig kafkaClientConfig = mock(KafkaClientConfig.class);
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

        return kafkaClientConfig;
    }

    @Bean
    @Primary
    public MDCProcessor mdcProcessor() {
        return mock(MDCProcessor.class);
    }

    @Bean
    @Primary
    public EmptyScanEntityProcessor emptyScanEntityProcessor() {
        return mock(EmptyScanEntityProcessor.class);
    }

    @Bean
    @Primary
    public RouteManager RouteManager() {
        return mock(RouteManager.class);
    }
}

package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistryHttp;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.HttpClient;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClientBuilder;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Data
public class ConfluentSchemaRegistryClientManagerImpl implements MessagingServiceClientManager<ConfluentSchemaRegistryHttp> {
    @Override
    public ConfluentSchemaRegistryHttp getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.debug("creating Confluent Schema Registry client for: {}", connectionDetailsEvent.getMessagingServiceId());
        HttpClient httpClient = HttpClient.builder()
                .webClient(HttpClientBuilder.create().build())
                .connectionUrl(connectionDetailsEvent.getUrl())
                .build();
        log.debug("Confluent Schema Registry client created for {}.", connectionDetailsEvent.getMessagingServiceId());
        return new ConfluentSchemaRegistryHttp(httpClient);
    }
}

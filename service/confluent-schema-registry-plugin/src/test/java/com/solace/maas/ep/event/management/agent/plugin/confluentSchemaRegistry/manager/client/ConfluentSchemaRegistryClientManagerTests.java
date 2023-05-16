package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistryHttp;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("TEST")
class ConfluentSchemaRegistryClientManagerTests {
    private final ConfluentSchemaRegistryClientManagerImpl confluentSchemaRegistryClientManager =
            new ConfluentSchemaRegistryClientManagerImpl();

    @Test
    void createConfluentSchemaRegistryClientTest() {
        ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                .url("localhost:1000")
                .messagingServiceId("messaging_service_id")
                .name("conn_name")
                .authenticationDetails(List.of())
                .build();

        ConfluentSchemaRegistryHttp confluentSchemaRegistryHttp = confluentSchemaRegistryClientManager.getClient(connectionDetailsEvent);
        assertThat(confluentSchemaRegistryHttp.getHttpClient().getWebClient()).isNotNull();
        assertThat(confluentSchemaRegistryHttp.getHttpClient().getConnectionUrl()).isEqualTo("localhost:1000");
    }
}

package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.manager.processor;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event.ConfluentSchemaRegistrySchemaEvent;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistryHttp;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistrySchemaProcessor;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(classes = main.java.com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.ConfluentSchemaRegistryApplication.class)
public class ConfluentSchemaRegistrySchemaProcessorTests {
    @Mock
    ConfluentSchemaRegistryHttp confluentSchemaRegistryHttp;
    @Mock
    private MessagingServiceDelegateService messagingServiceDelegateService;
    @InjectMocks
    private ConfluentSchemaRegistrySchemaProcessor confluentSchemaRegistrySchemaProcessor;

    @SneakyThrows
    @Test
    public void testHandleEvent() {
        ConfluentSchemaRegistrySchemaEvent schema1 = ConfluentSchemaRegistrySchemaEvent.builder()
                .schema("{}")
                .subject("subj1")
                .build();
        ConfluentSchemaRegistrySchemaEvent schema2 = ConfluentSchemaRegistrySchemaEvent.builder()
                .schema("{}")
                .subject("subj2")
                .build();
        when(messagingServiceDelegateService.getMessagingServiceClient("testService"))
                .thenReturn(confluentSchemaRegistryHttp);
        when(confluentSchemaRegistryHttp.getSchemas()).thenReturn(List.of(
                schema1, schema2));
        List<ConfluentSchemaRegistrySchemaEvent> schemaEvents = confluentSchemaRegistrySchemaProcessor.handleEvent(
                Map.of(RouteConstants.MESSAGING_SERVICE_ID, "testService"), new Object());
        assertThat(schemaEvents, hasSize(2));
        assertThat(schemaEvents, containsInAnyOrder(schema1, schema2));
        assertThatNoException();
    }
}

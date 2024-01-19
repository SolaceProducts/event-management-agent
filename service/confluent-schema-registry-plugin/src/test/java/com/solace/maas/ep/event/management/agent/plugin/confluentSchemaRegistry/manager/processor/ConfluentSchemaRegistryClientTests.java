package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.manager.processor;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event.ConfluentSchemaRegistrySchemaEvent;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistryHttp;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.HttpClient;
import com.solace.maas.ep.event.management.agent.plugin.exception.PluginClientException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("TEST")
@SpringBootTest(classes = main.java.com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.ConfluentSchemaRegistryApplication.class)
@Slf4j
class ConfluentSchemaRegistryClientTests {

    static private MockWebServer mockWebServer;

    static private ConfluentSchemaRegistryHttp confluentSchemaRegistryHttp;

    private static String VALID_RESPONSE;

    static {
        try {
            VALID_RESPONSE = new String(requireNonNull(ConfluentSchemaRegistryClientTests.class
                    .getClassLoader()
                    .getResourceAsStream("schemas.json"))
                    .readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @BeforeAll
    static void setupMocks() {
        mockWebServer = new MockWebServer();
        mockWebServer.start(12345);

        CloseableHttpClient mockClient = HttpClients.createDefault();

        HttpClient baseClient = HttpClient.builder()
                .connectionUrl("http://localhost:12345")
                .webClient(mockClient)
                .build();

        confluentSchemaRegistryHttp = new ConfluentSchemaRegistryHttp(baseClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @SneakyThrows
    @Test
    void getAllSchemasTest() {

        mockWebServer.enqueue(new MockResponse()
                .setBody(VALID_RESPONSE)
                .setResponseCode(200));

        List<ConfluentSchemaRegistrySchemaEvent> confluentSchemaRegistrySchemaEventList = confluentSchemaRegistryHttp.getSchemas();
        assertThat(confluentSchemaRegistrySchemaEventList).hasSize(4);
        // schemaType for avro is null
        assertNull(confluentSchemaRegistrySchemaEventList.get(0).getSchemaType());
        assertNull(confluentSchemaRegistrySchemaEventList.get(1).getSchemaType());
        // schemaType for non-avro is not null
        assertNotNull(confluentSchemaRegistrySchemaEventList.get(2).getSchemaType());
        assertNotNull(confluentSchemaRegistrySchemaEventList.get(3).getSchemaType());

        assertThat(confluentSchemaRegistrySchemaEventList.get(1).getReferences()).hasSize(1);

        //Data type for id and version and reference-version is Integer
        assertThat(confluentSchemaRegistrySchemaEventList.get(1).getId()).isInstanceOf(Integer.class);
        assertThat(confluentSchemaRegistrySchemaEventList.get(1).getVersion()).isInstanceOf(Integer.class);
        assertThat(confluentSchemaRegistrySchemaEventList.get(1).getReferences().get(0).getVersion()).isInstanceOf(Integer.class);
    }

    @SneakyThrows
    @Test
    void getAllSchemasFailTest() {

        mockWebServer.enqueue(new MockResponse()
                .setBody("service unavailable")
                .setResponseCode(503));
        assertThrows(PluginClientException.class, () -> confluentSchemaRegistryHttp.getSchemas());
    }
}

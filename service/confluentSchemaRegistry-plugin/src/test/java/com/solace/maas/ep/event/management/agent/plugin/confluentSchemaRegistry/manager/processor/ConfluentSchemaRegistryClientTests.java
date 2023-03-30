package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.manager.processor;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event.ConfluentSchemaRegistrySchemaEvent;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistryHttp;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.HttpClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(classes = main.java.com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.ConfluentSchemaRegistryApplication.class)
@Slf4j
class ConfluentSchemaRegistryClientTests {
    private final Map<String, String> requestResponseMap = buildRequestResponseMap();
    private final DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
    @Captor
    private ArgumentCaptor<Function<UriBuilder, URI>> uriFunctionCaptor;
    @Mock
    private WebClient mockClient;

    private ConfluentSchemaRegistryHttp confluentSchemaRegistryHttp;

    @BeforeEach
    void setupMocks() {
        HttpClient baseClient = HttpClient.builder()
                .connectionUrl("http://myHost:12345")
                .webClient(mockClient)
                .build();

        mockHttp(mockClient);

        confluentSchemaRegistryHttp = new ConfluentSchemaRegistryHttp(baseClient);
    }

    @SneakyThrows
    @Test
    void getAllSchemasTest() {
        List<ConfluentSchemaRegistrySchemaEvent> confluentSchemaRegistrySchemaEventList = confluentSchemaRegistryHttp.getSchemas();
        assertThat(confluentSchemaRegistrySchemaEventList).hasSize(3);
        assertThatNoException();
    }

    private void mockHttp(WebClient webClient) {
        WebClient.RequestHeadersUriSpec request = mock(WebClient.RequestHeadersUriSpec.class);
        when(webClient.get()).thenReturn(request);

        when(request.uri(uriFunctionCaptor.capture())).thenAnswer(
                (Answer) invocation -> {
                    URI newUri = uriFunctionCaptor.getValue().apply(defaultUriBuilderFactory.builder());
                    return createWebClientResponse(requestResponseMap.get(newUri.toString()));
                }
        );
    }

    private WebClient.RequestHeadersSpec createWebClientResponse(String response) {
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(any(MediaType.class))).thenReturn(requestHeadersSpec);

        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mono monoSubjectList = mock(Mono.class);

        when(responseSpec.bodyToMono(any(Class.class))).thenReturn(monoSubjectList);
        when(monoSubjectList.block()).thenReturn(response);

        return requestHeadersSpec;
    }

    private Map<String, String> buildRequestResponseMap() {
        Map<String, String> requestResponseResult = new HashMap<>();

        // Add response for get all schemas
        requestResponseResult.put("http://myHost:12345/schemas", loadResourceFileAsString("schemas.json"));
        return requestResponseResult;
    }

    protected String loadResourceFileAsString(String filePath) {
        Path resourceDirectory = Paths.get("src", "test", "resources", filePath);
        Path path = resourceDirectory.toFile().toPath();
        try {
            return Files.readString(path);
        } catch (FileNotFoundException e) {
            log.error("Could not find resource file " + filePath, e);
            fail(String.format("Could not find resource file %s", filePath));
        } catch (IOException e) {
            log.error("Error reading resource file " + filePath, e);
            fail(String.format("Error reading resource file %s", filePath));
        }
        return null;
    }
}

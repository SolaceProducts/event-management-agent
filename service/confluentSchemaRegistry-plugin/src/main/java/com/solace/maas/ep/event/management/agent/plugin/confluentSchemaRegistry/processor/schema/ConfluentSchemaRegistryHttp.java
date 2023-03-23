package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event.ConfluentSchemaRegistrySchemaEvent;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Slf4j
@Getter
public class ConfluentSchemaRegistryHttp {
    private final static String GET_ALL_SCHEMAS = "/schemas";
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public ConfluentSchemaRegistryHttp(HttpClient httpClient) {
        this.httpClient = httpClient;
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<ConfluentSchemaRegistrySchemaEvent> getSchemas() throws JsonProcessingException {
        Map<String, String> substitutionMap = new HashMap<>();
        String response = getResponse(createUriBuilderFunction(GET_ALL_SCHEMAS, substitutionMap));
        List<ConfluentSchemaRegistrySchemaEvent> confluentSchemaRegistrySchemaEventList =
                objectMapper.readValue(response, new TypeReference<>() {
                });
        return confluentSchemaRegistrySchemaEventList;
    }

    private Function<UriBuilder, URI> createUriBuilderFunction(String uriPath,
                                                               Map<String, String> substitutionMap) {
        URI uri = getUri();
        return (uriBuilder) -> uriBuilder
                .path(uriPath)
                .host(uri.getHost())
                .port(uri.getPort())
                .scheme(uri.getScheme())
                .build(substitutionMap);
    }

    private URI getUri() {
        URI uri = null;
        try {
            uri = new URI(httpClient.getConnectionUrl());
        } catch (URISyntaxException e) {
            log.error("URI error for {}", httpClient.getConnectionUrl(), e);
            throw new RuntimeException(String.format("Could not construct URL from %s", httpClient.getConnectionUrl()), e);
        }
        return uri;
    }

    private String getResponse(Function<UriBuilder, URI> uriMethod) {
        String response = httpClient.getWebClient()
                .get()
                .uri(uriMethod)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}

package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.event.ConfluentSchemaRegistrySchemaEvent;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Slf4j
@Getter
public class ConfluentSchemaRegistryHttp {
    private static final String GET_ALL_SCHEMAS = "/schemas";
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public ConfluentSchemaRegistryHttp(HttpClient httpClient) {
        this.httpClient = httpClient;
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<ConfluentSchemaRegistrySchemaEvent> getSchemas() throws JsonProcessingException {
        Map<String, String> substitutionMap = new HashMap<>();
        String response = getResponse(getUri(GET_ALL_SCHEMAS));
        return objectMapper.readValue(response, new TypeReference<>() {
        });
    }

    private URI getUri(String path) {
        URI uri;
        try {
            uri = new URI(httpClient.getConnectionUrl() + path);
        } catch (URISyntaxException e) {
            log.error("URI error for {}", httpClient.getConnectionUrl(), e);
            throw new RuntimeException(String.format("Could not construct URL from %s", httpClient.getConnectionUrl()), e);
        }
        return uri;
    }

    private String getResponse(URI uri) {
        HttpGet getData = new HttpGet();
        getData.setURI(uri);
        try (CloseableHttpResponse response = httpClient.getWebClient().execute(getData)) {
            if (response.getStatusLine().getStatusCode() != 200) {

            }
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

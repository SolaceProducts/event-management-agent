package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema;

import lombok.Builder;
import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;

@Data
@Builder
public class HttpClient {
    private CloseableHttpClient webClient;
    private String connectionUrl;
}

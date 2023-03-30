package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Builder
public class HttpClient {
    private WebClient webClient;
    private String connectionUrl;
}

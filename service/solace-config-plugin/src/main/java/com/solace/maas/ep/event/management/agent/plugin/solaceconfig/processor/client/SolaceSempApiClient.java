package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolaceSempRestClientConfig {
    private ApiClient apiClient;
    private String baseUrl;
    private String userName;
    private String password;
}

package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.HttpBasicAuth;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SempApiClient {
    private ApiClient apiClient;
    private String baseurl;
    private String username;
    private String password;
    private String msgVpn;

    public static SempApiClientBuilder builder() {
        return new CustomSempApiClientBuilder();
    }

    private static class CustomSempApiClientBuilder extends SempApiClientBuilder {

        @Override
        public SempApiClient build() {
            super.apiClient.setBasePath(super.baseurl);
            super.apiClient.setPassword(super.password);
            super.apiClient.setUsername(super.username);
            HttpBasicAuth basicAuth = (HttpBasicAuth) super.apiClient.getAuthentication("basicAuth");
            basicAuth.setUsername(super.username);
            basicAuth.setPassword(super.password);
            return super.build();
        }

    }
}

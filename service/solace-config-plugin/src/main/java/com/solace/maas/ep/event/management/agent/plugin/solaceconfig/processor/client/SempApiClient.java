package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.HttpBasicAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AboutApi;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AllApi;

@Configuration
public class SolaceSempRestClient {
    @Bean
    public AboutApi getAboutApi(){
        return new AboutApi();
    }
    @Bean
    public AllApi getAllApi(){
        return new AllApi();
    }

    @Bean
    public ApiClient getApiClient(){
        ApiClient client = new ApiClient();
        client.setBasePath("http://www.solace.com/SEMP/v2/config");
        HttpBasicAuth basicAuth = (HttpBasicAuth)client.getAuthentication("basicAuth");
        basicAuth.setUsername("admin");
        basicAuth.setPassword("default");
        return client;
    }
}

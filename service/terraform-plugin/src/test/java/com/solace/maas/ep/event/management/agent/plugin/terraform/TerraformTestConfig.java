package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClientFactory;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

public class TerraformTestConfig {
    @Bean
    @Primary
    public TerraformClient getTerraformClient() {
        return mock(TerraformClient.class);
    }

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public TerraformProperties getTfProperties() {
        return new TerraformProperties();
    }

    @Bean
    @Primary
    public TerraformLogProcessingService getTfLogProcessingService(ObjectMapper objectMapper, TerraformProperties terraformProperties) {
        return new TerraformLogProcessingService(objectMapper, terraformProperties);
    }

    @Bean
    @Primary
    public TerraformClientFactory getTfClientFactory(TerraformClient terraformClient) {
        return () -> terraformClient;
    }

    @Bean
    @Primary
    public TerraformManager getTfManager(TerraformLogProcessingService terraformLogProcessingService,
                                         TerraformProperties terraformProperties, TerraformClientFactory terraformClientFactory) {
        return new TerraformManager(terraformLogProcessingService, terraformProperties, terraformClientFactory);
    }
}

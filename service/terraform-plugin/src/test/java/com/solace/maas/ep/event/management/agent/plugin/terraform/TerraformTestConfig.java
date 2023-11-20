package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
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
    public TerraformLogProcessingService getTfLogProcessingService() {
        return mock(TerraformLogProcessingService.class);
    }

    @Bean
    @Primary
    public TerraformManager getTfManager(TerraformClient tfClient, TerraformLogProcessingService terraformLogProcessingService) {
        return new TerraformManager(tfClient, terraformLogProcessingService);
    }

}

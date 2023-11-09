package com.solace.maas.ep.event.management.agent.plugin.terraform.configuration;

import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TerraformConfiguration {
    @Bean
    public TerraformClient terraformClient() {
        return new TerraformClient();
    }
}

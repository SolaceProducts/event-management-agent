package com.solace.maas.ep.event.management.agent.plugin.terraform.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:command-configs.properties")
public class TerraformPropertiesFromFile {
}

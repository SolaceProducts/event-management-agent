package com.solace.maas.ep.event.management.agent.plugin.terraform.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Data
@Configuration
@ConfigurationProperties(prefix = "plugins.terraform")
public class TerraformProperties {
    private String workingDirectoryRoot = Optional.ofNullable(System.getenv("HOME")).orElse("/tmp") + "/config";
}

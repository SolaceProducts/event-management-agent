package com.solace.maas.ep.event.management.agent.plugin.terraform.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
public class TerraformProperties {
    @Value("${COMMAND_PATH:${HOME}/tfconfig}")
    private String workingDirectoryRoot;
}

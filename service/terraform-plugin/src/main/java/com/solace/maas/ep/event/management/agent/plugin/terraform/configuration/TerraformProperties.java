package com.solace.maas.ep.event.management.agent.plugin.terraform.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
public class TerraformProperties {
    @Value("${plugins.terraform.workingDirectoryRoot:/${HOME}/tfconfig}")
    private String workingDirectoryRoot;
}

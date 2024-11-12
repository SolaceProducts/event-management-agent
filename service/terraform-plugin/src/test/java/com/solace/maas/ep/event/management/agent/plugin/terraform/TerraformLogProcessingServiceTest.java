package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class TerraformLogProcessingServiceTest {


    @Test
    void testBuildTfStateFileDeletionFailureResult(){
        RuntimeException rootCause = new RuntimeException("rootCause");
        ObjectMapper objectMapper = new ObjectMapper();
        TerraformLogProcessingService terraformLogProcessingService = new TerraformLogProcessingService(objectMapper);
        CommandResult result = terraformLogProcessingService.buildTfStateFileDeletionFailureResult(rootCause);
        assertThat(result.getStatus()).isEqualTo(JobStatus.error);
        assertThat(result.getLogs().size()).isEqualTo(1);
        Map<String, Object> log = result.getLogs().get(0);
        assertThat(log.get("message")).isEqualTo("Failed removing Terraform state: rootCause");
        assertThat(log.get("level")).isEqualTo("ERROR");
        assertThat(log.get("timestamp")).isNotNull();
    }
}

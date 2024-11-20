package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("TEST")
class TerraformUtilsTest {

    @Test
    void testDeleteConfigPath(@TempDir Path tempDirectory) throws IOException {
        CommandRequest commandRequest = CommandRequest
                .builder()
                .context("someContext")
                .serviceId("serviceId")
                .build();
        // by convention, the directory name is someContext-serviceId
        Path subDirectory = tempDirectory.resolve("someContext-serviceId");
        // create sample directories in tempDirectory
        Files.createDirectory(subDirectory);
        assertTrue(Files.exists(subDirectory));
        TerraformUtils.deleteConfigPath(commandRequest, tempDirectory.toString());
        assertTrue(Files.notExists(subDirectory));
        assertTrue(Files.exists(tempDirectory));
    }
}

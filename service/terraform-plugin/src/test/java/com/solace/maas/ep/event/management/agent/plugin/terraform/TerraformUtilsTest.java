package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("TEST")
public class TerraformUtilsTest {

    @Test
    public void testDeleteConfigPath() throws IOException {
        CommandRequest commandRequest = CommandRequest
                .builder()
                .context("someContext")
                .serviceId("serviceId")
                .build();
        Path tempDirectory = Files.createTempDirectory("tfDeleteMeDirectory");
        // by convention, the directory name is someContext-serviceId
        Path subDirectory = tempDirectory.resolve("someContext-serviceId");
        // create sample directories in tempDirectory
        Files.createDirectory(subDirectory);
        assertTrue(Files.exists(subDirectory));
        TerraformUtils.deleteConfigPath(commandRequest, tempDirectory.toString());
        assertTrue(Files.notExists(subDirectory));
        assertTrue(Files.exists(tempDirectory));
        //cleanup
        Files.deleteIfExists(tempDirectory);
        assertFalse(Files.exists(tempDirectory));
    }
}

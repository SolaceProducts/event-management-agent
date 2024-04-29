package com.solace.maas.ep.event.management.agent.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.Assert.assertThrows;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class TerraformUtilsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testConvertGenericLogMessageToTFStyleMessage() throws JsonProcessingException {
        String msg = TerraformUtils.convertGenericLogMessageToTFStyleMessage("This is a test", "info", objectMapper);
        Map<String, Object> logMsgMap = objectMapper.readValue(msg, Map.class);
        Assertions.assertThat(logMsgMap).containsOnlyKeys("@message", "@level", "@timestamp");
        Assertions.assertThat(logMsgMap.get("@message")).isEqualTo("This is a test");
        Assertions.assertThat(logMsgMap.get("@level")).isEqualTo("info");

        String errorMsg = TerraformUtils.convertGenericLogMessageToTFStyleMessage("This is a test for error", "error", objectMapper);
        Map<String, Object> logErrorMsgMap = objectMapper.readValue(errorMsg, Map.class);
        Assertions.assertThat(logErrorMsgMap).containsOnlyKeys("@message", "@level", "@timestamp");
        Assertions.assertThat(logErrorMsgMap.get("@message")).isEqualTo("This is a test for error");
        Assertions.assertThat(logErrorMsgMap.get("@level")).isEqualTo("error");

        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () ->
                TerraformUtils.convertGenericLogMessageToTFStyleMessage("This is a test for error", null, objectMapper)
        );

        Assertions.assertThat(thrown.getMessage()).isEqualTo("Unsupported log level null");

        thrown = assertThrows(UnsupportedOperationException.class, () ->
                TerraformUtils.convertGenericLogMessageToTFStyleMessage("This is a test for error", "", objectMapper)
        );

        Assertions.assertThat(thrown.getMessage()).isEqualTo("Unsupported log level ");

    }

    @Test
    void testCreateCommandExecutionLogDir(@TempDir Path configPath) throws IOException {
        Assertions.assertThat(configPath.resolve("executionLogs")).doesNotExist();
        Path created = TerraformUtils.createCommandExecutionLogDir(configPath);
        Assertions.assertThat(configPath.resolve("executionLogs")).exists();
        Path logFilePath = Files.writeString(created.resolve("a.log"), "some texts");
        //If the path already exists, it will not be recreated
        TerraformUtils.createCommandExecutionLogDir(configPath);
        Assertions.assertThat(configPath.resolve("executionLogs")).exists();
        Assertions.assertThat(logFilePath).exists();
    }
}

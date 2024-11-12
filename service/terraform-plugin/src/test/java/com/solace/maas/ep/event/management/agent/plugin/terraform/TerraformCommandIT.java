package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TerraformTestConfig.class)

public class TerraformCommandIT {
    @SpyBean
    private TerraformManager terraformManager;

    @Autowired
    private TerraformClient terraformClient;

    @Autowired
    private TerraformProperties terraformProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeAll
    public void setup() {
        terraformProperties.setWorkingDirectoryRoot(System.getProperty("java.io.tmpdir"));
    }

    @AfterEach
    public void reset_mocks() {
        Mockito.reset(terraformClient);
    }


    @Test
    public void testWriteHCLWithDefaultFile() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command command = generateCommand("write_HCL", newQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(command);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the file was written
        String content = Files.readString(Path.of(terraformProperties.getWorkingDirectoryRoot() + "/app123-ms1234/config.tf"));
        assertEquals(content, newQueueTf);
    }

    @Test
    void testExecutionLogsAreGenerated() throws IOException {
        List<String> newQueueTfLogs = getResourceAsStringArray(resourceLoader.getResource("classpath:tfLogs/tfAddHappyPath.txt"));
        Path executionLogPath = Paths.get(terraformProperties.getWorkingDirectoryRoot() + "/app123-ms1234/executionLogs/");
        FileUtils.deleteDirectory(executionLogPath.toFile());
        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command writeHclCommand = generateCommand("write_HCL", newQueueTf);
        Path writeHclExecutionLogFilePath = terraformManager.execute(generateCommandRequest(writeHclCommand), writeHclCommand, Map.of());
        Assertions.assertThat(writeHclExecutionLogFilePath.toFile().getName()).startsWith("write_HCL");


        Command applyCommand = generateCommand("apply", newQueueTf);

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        when(terraformClient.apply(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        // Setup the output
        setupLogMock(newQueueTfLogs);

        Path applyExecutionLogFilePath = terraformManager.execute(generateCommandRequest(applyCommand), applyCommand, Map.of());
        Assertions.assertThat(applyExecutionLogFilePath.toFile().getName()).startsWith("apply");

        Command syncCommand = generateCommand("sync", newQueueTf, true);
        // Setup the output
        setupLogMock(newQueueTfLogs);
        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> false));

        Path syncExecutionLogFilePath = terraformManager.execute(generateCommandRequest(syncCommand), syncCommand, Map.of());
        Assertions.assertThat(syncExecutionLogFilePath.toFile().getName()).startsWith("sync");

        Assertions.assertThat(Files.readAllLines(writeHclExecutionLogFilePath)).hasSize(1);
        Assertions.assertThat(Files.readAllLines(applyExecutionLogFilePath)).hasSizeGreaterThan(1);
        Assertions.assertThat(Files.readAllLines(syncExecutionLogFilePath)).hasSize(1);


    }

    @Test
    public void testWriteHCLWithSpecifiedFile() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command command = generateCommand("write_HCL", newQueueTf, false,
                Map.of("Content-Type", "application/hcl",
                        "Content-Encoding", "base64",
                        "Output-File-Name", "strangeFileName.tf"));

        CommandRequest terraformRequest = generateCommandRequest(command);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the file was written
        String content = Files.readString(Path.of(terraformProperties.getWorkingDirectoryRoot() + "/app123-ms1234/strangeFileName.tf"));

        assertEquals(content, newQueueTf);
    }

    @Test
    public void testCreateResourceHappyPath() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));
        List<String> newQueueTfLogs = getResourceAsStringArray(resourceLoader.getResource("classpath:tfLogs/tfAddHappyPath.txt"));

        Command command = generateCommand("apply", newQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(command);

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        when(terraformClient.apply(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));

        // Setup the output
        setupLogMock(newQueueTfLogs);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan and apply apis are called
        verify(terraformClient, times(1)).apply(any());
        verify(terraformManager, times(20)).logToConsole(any());

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.success, result.getStatus());
                assertTrue(result.getLogs().get(2).get("message").toString().contains("Creation complete after"));
                assertTrue(result.getLogs().get(3).get("message").toString().contains("Creation complete after"));
                assertAllLogsContainExpectedFields(result.getLogs());
            }
        }

    }

    @Test
    public void testApplyWithEnvVarsInParameters() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));
        List<String> newQueueTfLogs = getResourceAsStringArray(resourceLoader.getResource("classpath:tfLogs/tfAddHappyPath.txt"));

        Command command = generateCommand("apply", newQueueTf, false,
                Map.of("Content-Type", "application/hcl",
                        "Content-Encoding", "base64",
                        "environment", Map.of(
                                "orgId", "org123",
                                "runtimeAgentId", "ra123")));
        CommandRequest terraformRequest = generateCommandRequest(command);

        when(terraformClient.apply(any())).thenReturn(CompletableFuture.supplyAsync(() -> true));

        // Setup the output
        setupLogMock(newQueueTfLogs);

        terraformManager.execute(terraformRequest, command, new HashMap<>());

        ArgumentCaptor<Map<String, String>> envArgCaptor = ArgumentCaptor.forClass(Map.class);

        // Validate that the plan and apply apis are called
        verify(terraformClient, times(1)).apply(envArgCaptor.capture());

        assert (envArgCaptor.getValue().containsKey("TF_VAR_orgId"));
        assertEquals("org123", envArgCaptor.getValue().get("TF_VAR_orgId"));
        assert (envArgCaptor.getValue().containsKey("TF_VAR_runtimeAgentId"));
        assertEquals("ra123", envArgCaptor.getValue().get("TF_VAR_runtimeAgentId"));

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.success, result.getStatus());
                assertTrue(result.getLogs().get(2).get("message").toString().contains("Creation complete after"));
                assertTrue(result.getLogs().get(3).get("message").toString().contains("Creation complete after"));
                assertAllLogsContainExpectedFields(result.getLogs());
            }
        }
    }

    @Test
    public void testImportResourceWithSomeResourcesExistingAndSomeNot() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));
        List<String> partialImportTfLogs = getResourceAsStringArray(resourceLoader.getResource("classpath:tfLogs/tfPartialImport.txt"));

        Command command = generateCommand("sync", newQueueTf, true);
        CommandRequest terraformRequest = generateCommandRequest(command);

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> false));

        // Setup the output
        setupLogMock(partialImportTfLogs);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan and apply apis are called
        verify(terraformClient, times(1)).plan(any());
        verify(terraformManager, times(0)).logToConsole(any());

        String expectedImportTf = "import {\n" +
                "\tto = solacebroker_msg_vpn_queue_subscription.sub_66e36954c9ee1b2012bb57c8d6f6a2429e8b5aa81d48055767ef9c10ab2b074a\n" +
                "\tid = \"default/MyConsumer1/a%2Fb%2Fc\"\n" +
                "}";

        String content = Files.readString(Path.of(terraformProperties.getWorkingDirectoryRoot() + "/app123-ms1234/sync.tf"));
        assertEquals(content, expectedImportTf);

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {
                CommandResult result = tfCommand.getResult();
                assertNull(result);
            }
        }
    }

    @Test
    public void testExitOnFailureWhenFailingToWriteHCL() throws IOException {
        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command command = generateCommand("write_HCL", newQueueTf);
        Command command2 = generateCommand("apply", newQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(List.of(command, command2), true);

        // Fail the 1st request by setting the working directory to and invalid path
        String originalPath = terraformProperties.getWorkingDirectoryRoot();
        terraformProperties.setWorkingDirectoryRoot("/invalid/path");
        terraformManager.execute(terraformRequest, command, Map.of());
        terraformProperties.setWorkingDirectoryRoot(originalPath);

        // The first command should be failed, the second should not be executed
        // so the result is not set
        CommandResult result = terraformRequest.getCommandBundles().get(0).getCommands().get(0).getResult();
        assertEquals(JobStatus.error, result.getStatus());
        assertAllLogsContainExpectedFields(result.getLogs());
        verify(terraformClient, times(0)).apply(any());

        // The second command should not be executed
        CommandResult result2 = terraformRequest.getCommandBundles().get(0).getCommands().get(1).getResult();
        assertNull(result2);
    }

    @Test
    public void testCreateResourceTerraformErrorFailurePath() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));
        List<String> newQueueTfLogs = getResourceAsStringArray(resourceLoader.getResource("classpath:tfLogs/tfAddErrorSubscriptionAlreadyPresent.txt"));

        Command command = generateCommand("apply", newQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(command);

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        when(terraformClient.apply(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));

        // Setup the output
        setupLogMock(newQueueTfLogs);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan api is called
        verify(terraformClient, times(1)).apply(any());

        // Validate that the plan and apply apis are called
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.error, result.getStatus());
                List<Map<String, Object>> errorLogs = result.getLogs().stream()
                        .filter(log -> log.get("level").equals("ERROR"))
                        .toList();
                assertTrue(errorLogs.get(0).get("diagnosticDetail").toString().contains("Subscription a/b/c/> already exists"));
                assertAllLogsContainExpectedFields(result.getLogs());
            }
        }
    }

    @Test
    public void testCreateResourceMissingParameterFailurePath() {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        // Generate a command without the expected base64 parameters
        Command command = generateCommand("apply", newQueueTf, false, Map.of());
        CommandRequest terraformRequest = generateCommandRequest(command);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan and apply apis are called
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.error, result.getStatus());
                assertTrue(result.getLogs().get(0).get("errorType").toString().contains("java.lang.IllegalArgumentException"));
                assertTrue(result.getLogs().get(0).get("message").toString().contains("Missing Content-Encoding property in command parameters."));
                assertAllLogsContainExpectedFields(result.getLogs());
            }
        }
    }

    @Test
    public void testCreateResourceNoLogsFailurePath() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command command = generateCommand("apply", newQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(command);

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        when(terraformClient.apply(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));

        // Setup the empty output
        setupLogMock(List.of());

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan api is called
        verify(terraformClient, times(1)).apply(any());

        // Validate that the plan and apply apis are called
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.error, result.getStatus());
                assertTrue(result.getLogs().get(0).get("errorType").toString().contains("java.lang.IllegalArgumentException"));
                assertTrue(result.getLogs().get(0).get("message").toString().contains("No terraform logs were collected. Unable to process response."));
                assertAllLogsContainExpectedFields(result.getLogs());
            }
        }
    }

    @Test
    public void testCreateResourceUnknownCommandFailurePath() {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command command = generateCommand("appply", newQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(command);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan and apply apis are called
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.error, result.getStatus());
                assertTrue(result.getLogs().get(0).get("errorType").toString().contains("java.lang.IllegalArgumentException"));
                assertTrue(result.getLogs().get(0).get("message").toString().contains("Unsupported command appply"));
                assertAllLogsContainExpectedFields(result.getLogs());
            }
        }
    }

    @Test
    public void testIgnoreResult() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));
        List<String> newQueueTfLogs = getResourceAsStringArray(resourceLoader.getResource("classpath:tfLogs/tfAddHappyPath.txt"));

        Command command = generateCommand("apply", newQueueTf, true);
        CommandRequest terraformRequest = generateCommandRequest(command);

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        when(terraformClient.apply(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));

        // Setup the output
        setupLogMock(newQueueTfLogs);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan and apply apis are called
        verify(terraformClient, times(1)).apply(any());

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertNull(result);
            }
        }
    }

    @Test
    public void testDeleteResourceHappyPath() throws IOException {

        String deleteQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/deleteQueue.tf"));
        List<String> deleteQueueTfLogs = getResourceAsStringArray(resourceLoader.getResource("classpath:tfLogs/tfDeleteHappyPath.txt"));

        Command command = generateCommand("apply", deleteQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(command);

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        when(terraformClient.apply(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));

        // Setup the output
        setupLogMock(deleteQueueTfLogs);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the plan api is called
        verify(terraformClient, times(1)).apply(any());

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.success, result.getStatus());
                assertTrue(result.getLogs().get(2).get("message").toString().contains("Destruction complete after"));
                assertTrue(result.getLogs().get(3).get("message").toString().contains("Destruction complete after"));
                assertAllLogsContainExpectedFields(result.getLogs());
            }
        }
    }

    private static Command generateCommand(String tfCommand, String body) {
        return generateCommand(tfCommand, body, false);
    }

    private static Command generateCommand(String tfCommand, String body, Boolean ignoreResult) {
        return generateCommand(tfCommand, body, ignoreResult,
                Map.of("Content-Type", "application/hcl",
                        "Content-Encoding", "base64"));
    }

    private static Command generateCommand(String tfCommand, String body, Boolean ignoreResult, Map<String, Object> parameters) {
        return Command.builder()
                .body(Optional.ofNullable(body)
                        .map(b -> Base64.getEncoder().encodeToString(b.getBytes(UTF_8)))
                        .orElse(""))
                .command(tfCommand)
                .commandType(CommandType.terraform)
                .ignoreResult(ignoreResult)
                .parameters(parameters)
                .build();
    }


    private static CommandRequest generateCommandRequest(Command commandRequest) {
        return generateCommandRequest(List.of(commandRequest), false);
    }

    private static CommandRequest generateCommandRequest(List<Command> commandRequests, boolean exitOnFailure) {
        return CommandRequest.builder()
                .commandBundles(List.of(
                        CommandBundle.builder()
                                .executionType(ExecutionType.serial)
                                .exitOnFailure(exitOnFailure)
                                .commands(commandRequests)
                                .build()))
                .commandCorrelationId("234")
                .context("app123")
                .serviceId("ms1234")
                .build();
    }

    private void setupLogMock(List<String> logs) {
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Consumer<String> listener = (Consumer<String>) arg0;
            logs.forEach(listener);
            return null;
        }).when(terraformClient).setOutputListener(any());
    }


    private static String getResourceAsString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static List<String> getResourceAsStringArray(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return Arrays.stream(FileCopyUtils.copyToString(reader).split("\n"))
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void assertAllLogsContainExpectedFields(List<Map<String, Object>> logs) {
        for (Map<String, Object> log : logs) {
            assertTrue(log.containsKey("message"));
            assertTrue(log.containsKey("level"));
            assertTrue(log.containsKey("timestamp"));
        }
    }
}

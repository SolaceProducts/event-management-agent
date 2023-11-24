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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TerraformTestConfig.class)

public class TerraformCommandIT {
    @Autowired
    private TerraformManager terraformManager;

    @Autowired
    private TerraformClient terraformClient;

    @Autowired
    private TerraformProperties terraformProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @AfterEach
    public void reset_mocks() {
        Mockito.reset(terraformClient);
    }

    @Test
    public void testWriteHCL() throws IOException {

        String newQueueTf = getResourceAsString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command command = generateCommand("write_HCL", newQueueTf);
        CommandRequest terraformRequest = generateCommandRequest(command);

        terraformManager.execute(terraformRequest, command, Map.of());

        // Validate that the file was written
        String content = Files.readString(Path.of(terraformProperties.getWorkingDirectoryRoot() + "/app123-ms1234/config.tf"));

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
        verify(terraformClient, times(1)).plan(any());
        verify(terraformClient, times(1)).apply(any());

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.success, result.getStatus());
                assertTrue(result.getLogs().get(2).get("message").toString().contains("Creation complete after"));
                assertTrue(result.getLogs().get(3).get("message").toString().contains("Creation complete after"));
            }
        }
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
        verify(terraformClient, times(1)).plan(any());
        verify(terraformClient, times(1)).apply(any());

        // Validate that the plan and apply apis are called
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.error, result.getStatus());
                assertTrue(result.getErrors().get(0).get("diagnosticDetail").toString().contains("Subscription a/b/c/> already exists"));
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
                assertTrue(result.getErrors().get(0).get("errorType").toString().contains("java.lang.IllegalArgumentException"));
                assertTrue(result.getErrors().get(0).get("message").toString().contains("Missing Content-Encoding property in command parameters."));
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
        verify(terraformClient, times(1)).plan(any());
        verify(terraformClient, times(1)).apply(any());

        // Validate that the plan and apply apis are called
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.error, result.getStatus());
                assertTrue(result.getErrors().get(0).get("errorType").toString().contains("java.lang.IllegalArgumentException"));
                assertTrue(result.getErrors().get(0).get("message").toString().contains("No terraform logs were collected. Unable to process response."));
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
                assertTrue(result.getErrors().get(0).get("errorType").toString().contains("java.lang.IllegalArgumentException"));
                assertTrue(result.getErrors().get(0).get("message").toString().contains("Unsupported command appply"));
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
        verify(terraformClient, times(1)).plan(any());
        verify(terraformClient, times(1)).apply(any());

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.success, result.getStatus());
                assertTrue(result.getLogs().isEmpty());
                assertTrue(result.getErrors().isEmpty());
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
        verify(terraformClient, times(1)).plan(any());
        verify(terraformClient, times(1)).apply(any());

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command tfCommand : commandBundle.getCommands()) {

                CommandResult result = tfCommand.getResult();
                assertEquals(JobStatus.success, result.getStatus());
                assertTrue(result.getLogs().get(2).get("message").toString().contains("Destruction complete after"));
                assertTrue(result.getLogs().get(3).get("message").toString().contains("Destruction complete after"));
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

    private static Command generateCommand(String tfCommand, String body, Boolean ignoreResult, Map<String, String> parameters) {
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
        return CommandRequest.builder()
                .commandBundles(List.of(
                        CommandBundle.builder()
                                .executionType(ExecutionType.serial)
                                .exitOnFailure(false)
                                .commands(List.of(commandRequest))
                                .build()))
                .correlationId("234")
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
}

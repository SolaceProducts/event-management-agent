package com.solace.maas.ep.event.management.agent.plugin.terraform;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import org.junit.jupiter.api.Test;
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
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TerraformTestConfig.class)

public class TerraformCommandIT {
    @Autowired
    private TerraformManager terraformManager;

    @Autowired
    private TerraformClient terraformClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testWriteHCL() throws IOException {

        String newQueueTf = asString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command commandRequest = Command.builder()
                .body(Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8)))
                .commandType(CommandType.terraform)
                .command("write_HCL")
                .build();
        CommandRequest terraformRequest = CommandRequest.builder()
                .commandBundles(List.of(
                        CommandBundle.builder()
                                .executionType("serial")
                                .exitOnFailure(false)
                                .commands(List.of(commandRequest))
                                .build()))
                .correlationId("234")
                .context("app123")
                .messagingServiceId("ms1234")
                .build();

        terraformManager.execute(terraformRequest, commandRequest, Map.of());

        // Validate that the file was written
        String content = Files.readString(Path.of("/tmp/config/app123-ms1234/config.tf"));

        assertEquals(content, newQueueTf);
    }

    @Test
    public void testCreateResourceHappyPath() throws IOException {

        String newQueueTf = asString(resourceLoader.getResource("classpath:tfFiles/newQueue.tf"));

        Command commandRequest = Command.builder()
                .body(Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8)))
                .command("apply")
                .build();
        CommandRequest terraformRequest = CommandRequest.builder()
                .commandBundles(List.of(
                        CommandBundle.builder()
                                .executionType("serial")
                                .exitOnFailure(false)
                                .commands(List.of(commandRequest))
                                .build()))
                .correlationId("234")
                .context("app123")
                .messagingServiceId("ms1234")
                .build();

        when(terraformClient.plan(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));
        when(terraformClient.apply(Map.of())).thenReturn(CompletableFuture.supplyAsync(() -> true));

        // Setup the output
        List<String> outputResult = List.of(
                "{ \"result1\": \"okay\" }",
                "{ \"result2\": \"okay\" }");
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Consumer<String> listener = (Consumer<String>) arg0;
            outputResult.stream().forEach(listener::accept);
            return null;
        }).when(terraformClient).setOutputListener(any());

        // Setup the error
        String errorResult = "{ \"error\": null }";
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Consumer<String> listener = (Consumer<String>) arg0;
            listener.accept(errorResult);
            return null;
        }).when(terraformClient).setErrorListener(any());

        terraformManager.execute(terraformRequest, commandRequest, Map.of());

        // Check the responses
//        for (Command command : terraformRequest.getCommands()) {
//            CommandResult result = command.getResult();
////            result.getLogs();
//        }
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

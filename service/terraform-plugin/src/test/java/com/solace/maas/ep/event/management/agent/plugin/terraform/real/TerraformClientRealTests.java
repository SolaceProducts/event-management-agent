package com.solace.maas.ep.event.management.agent.plugin.terraform.real;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TerraformClientRealTests {
    @Autowired
    private TerraformManager terraformManager;

    @Autowired
    private ResourceLoader resourceLoader;

    private final Map<String, String> tfEnvVars = Map.of(
            "TF_VAR_username", System.getenv("REAL_TEST_USERNAME"),
            "TF_VAR_password", System.getenv("REAL_TEST_PASSWORD"),
            "TF_VAR_url", System.getenv("REAL_TEST_URL")
    );

    @Test
    public void planCreateNewQueue() {
        executeTerraformCommand("addQueue.tf", "plan ");
    }

    @Test
    public void createNewQueue() {
        executeTerraformCommand("addQueue.tf", "apply");
    }

    @Test
    public void create2DifferentQueues() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<List<CommandBundle>> future1 = executorService.submit(() ->
                executeTerraformCommand("addQueue.tf", "apply"));
        Future<List<CommandBundle>> future2 = executorService.submit(() ->
                executeTerraformCommand("addQueue2.tf", "apply", "app123-consumer2"));
        // wait for the futures to complete
        try {
            List<CommandBundle> command1Bundles = future1.get();
            List<CommandBundle> command2Bundles = future2.get();
        } catch (Exception e) {
            log.error("Error waiting for futures to complete", e);
        }
    }

    @Test
    public void create2OfTheSameQueue() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<List<CommandBundle>> future1 = executorService.submit(() ->
                executeTerraformCommand("addQueue.tf", "apply", "app123-consumer", JobStatus.success));
        Future<List<CommandBundle>> future2 = executorService.submit(() ->
                executeTerraformCommand("addQueue.tf", "apply", "app123-consumer", JobStatus.error));
        // wait for the futures to complete
        try {
            List<CommandBundle> command1Bundles = future1.get();
            List<CommandBundle> command2Bundles = future2.get();
            // We expect the first one to succeed and the second one to fail
            assertEquals(JobStatus.success, command1Bundles.get(0).getCommands().get(0).getResult().getStatus());
            assertEquals(JobStatus.error, command2Bundles.get(0).getCommands().get(0).getResult().getStatus());
        } catch (Exception e) {
            log.error("Error waiting for futures to complete", e);
            fail();
        }
    }

    @Test
    public void delete2Queues() {
        executeTerraformCommand("deleteQueue.tf", "apply");
        executeTerraformCommand("deleteQueue2.tf", "apply", "app123-consumer2");

    }

    @Test
    public void deleteNewQueue() {
        executeTerraformCommand("deleteQueue.tf", "apply");
    }

    @Test
    public void updateNewQueue() {
        executeTerraformCommand("updateQueue.tf", "apply");
    }

    @Test
    public void importResource() {
        String newQueueTf = asString(resourceLoader.getResource("classpath:realTfFiles" + File.separator + "addQueue.tf"));

        Command commandRequest1 = Command.builder()
                .body(Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8)))
                .command("import")
                .parameters(Map.of(
                        "address", "solacebroker_msg_vpn_queue.fh71c08said-Consumer1",
                        "tfId", "default/Consumer1"
                ))
                .build();

        Command commandRequest2 = Command.builder()
                .body(Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8)))
                .command("import")
                .parameters(Map.of(
                        "address", "solacebroker_msg_vpn_queue_subscription.fh71c08said-Consumer1_a_b_c__",
                        "tfId", "default/Consumer1/a%2Fb%2Fc%2F%3E"
                ))
                .build();

        CommandRequest terraformRequest = CommandRequest.builder()
                .commandCorrelationId("abc123")
                .context("app123-consumer")
                .commandBundles(List.of(
                        CommandBundle.builder()
                                .commands(List.of(commandRequest1, commandRequest2))
                                .build())
                )
                .build();

        for (Command command : terraformRequest.getCommandBundles().get(0).getCommands()) {
            terraformManager.execute(terraformRequest, command, tfEnvVars);
        }

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command command : commandBundle.getCommands()) {
                CommandResult result = command.getResult();
                System.out.println("Logs " + result.getLogs());
                assertNotSame(JobStatus.error, result.getStatus());
            }
        }

    }

    private List<CommandBundle> executeTerraformCommand(String hclFileName, String tfVerb) {
        return executeTerraformCommand(hclFileName, tfVerb, "app123-consumer");
    }

    private List<CommandBundle> executeTerraformCommand(String hclFileName, String tfVerb, String context) {
        return executeTerraformCommand(hclFileName, tfVerb, context, JobStatus.success);
    }

    private List<CommandBundle> executeTerraformCommand(String hclFileName, String tfVerb, String context, JobStatus expectedJobStatus) {
        String terraformString = asString(resourceLoader.getResource("classpath:realTfFiles" + File.separator + hclFileName));

        Command commandRequest = Command.builder()
                .body(Base64.getEncoder().encodeToString(terraformString.getBytes(UTF_8)))
                .command(tfVerb)
                .parameters(Map.of(
                        "Content-Type", "application/hcl",
                        "Content-Encoding", "base64"))
                .build();
        CommandRequest terraformRequest = CommandRequest.builder()
                .commandBundles(List.of(CommandBundle.builder()
                        .commands(List.of(commandRequest))
                        .build()))
                .context(context)
                .serviceId("abc123")
                .commandCorrelationId("myCorrelationId")
                .build();

        for (Command command : terraformRequest.getCommandBundles().get(0).getCommands()) {
            terraformManager.execute(terraformRequest, command, tfEnvVars);
        }

        // Check the responses
        for (CommandBundle commandBundle : terraformRequest.getCommandBundles()) {
            for (Command command : commandBundle.getCommands()) {
                CommandResult result = command.getResult();
                System.out.println("Logs " + result.getLogs());
                assertEquals(expectedJobStatus, result.getStatus());
            }
        }

        return terraformRequest.getCommandBundles();
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

package com.solace.maas.ep.event.management.agent.commandManager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.resources.TopicSubscription;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol.epConfigPush;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@ActiveProfiles("gameday")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NegativeTerraformTests {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private EventPortalProperties eventPortalProperties;
    @Autowired
    private SolacePublisher solacePublisher;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private TerraformProperties terraformProperties;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final static String serviceId = "49a23700m80";
    private final Function3Arity<AtomicReference<CommandMessage>, AtomicBoolean, InboundMessage> handleCommandResponseMessage =
            (commandResponse, keepRunning, inboundMessage) -> {
                String messageAsString = inboundMessage.getPayloadAsString();
                try {
                    CommandMessage receivedCommandMessage = objectMapper.readValue(messageAsString, CommandMessage.class);
                    log.info("TEST: Received command response");
                    commandResponse.set(receivedCommandMessage);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } finally {
                    keepRunning.set(false);
                }
            };

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    public void createQueuePositiveTest() {
        String correlationId = "myCorrelationId";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueue.tf", serviceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);
    }

    @Test
    public void deleteQueuePositiveTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("deleteQueue.tf", serviceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadHostTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueueBadHost.tf", serviceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadPortTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueueBadPort.tf", serviceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadCredentialsTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueueBadCredentials.tf", serviceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueSempFailureTest() {

        // First create the queue
        String correlationId = "myCorrelationId";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueue.tf", serviceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);

        // Delete the terraform state file
        Path terraformStatePath = Paths.get(terraformProperties.getWorkingDirectoryRoot(),
                context + "-" + serviceId, "terraform.tfstate");
        try {
            Files.delete(terraformStatePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Now try to create the queue again
        correlationId = "myCorrelationId2";
        context = "abc123";
        commandResponse = runTest("addQueue.tf", serviceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badBlockHclFailureTest() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("badBlockHcl.tf", serviceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badFormatHclFailureTest() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("badFormatHcl.tf", serviceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void unknownCommandError() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("badFormatHcl.tf", serviceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }


    private CommandMessage runTest(String hclFileName, String serviceId, String correlationId) {
        return runTest(hclFileName, serviceId, correlationId, null);
    }

    private CommandMessage runTest(String hclFileName, String serviceId, String correlationId, String context) {

        String newQueueTf = asString(resourceLoader.getResource("classpath:hcl" + File.separator + hclFileName));
        String newQueueTfBase64 = Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8));

        CommandMessage message = createCommandMessage(serviceId, correlationId, context, newQueueTfBase64);

        AtomicBoolean keepRunning = new AtomicBoolean(true);
        AtomicReference<CommandMessage> commandResponse = new AtomicReference<>();
        setupMessageReceiver(commandResponse, keepRunning, handleCommandResponseMessage);

        log.info("Sending command");
        solacePublisher.publish(message,
                "sc/ep/runtime/" + eventPortalProperties.getOrganizationId() + "/" +
                        eventPortalProperties.getRuntimeAgentId() + "/command/v1/" +
                        correlationId);

        while (keepRunning.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return commandResponse.get();
    }

    private void setupMessageReceiver(AtomicReference<CommandMessage> commandMessageRef, AtomicBoolean keepRunning,
                                      Function3Arity<AtomicReference<CommandMessage>, AtomicBoolean, InboundMessage> function3Arity) {
        log.info("TEST: Starting receiver");
        DirectMessageReceiver directMessageReceiver = messagingService
                .createDirectMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of("sc/ep/runtime/" +
                        eventPortalProperties.getOrganizationId() + "/" +
                        eventPortalProperties.getRuntimeAgentId() +
                        "/commandResponse/v1/>"))
                .build()
                .start();

        directMessageReceiver.receiveAsync(inboundMessage -> function3Arity.apply(commandMessageRef, keepRunning, inboundMessage));
    }

    private void validateSuccessCommandResponse(CommandMessage commandResponse) {
        List<JobStatus> results = commandResponse.getCommandBundles().stream()
                .map(commandBundle ->
                        commandBundle.getCommands().stream()
                                .map(command -> command.getResult().getStatus())
                                .toList())
                .flatMap(List::stream)
                .toList();
        results.forEach(result -> {
            if (result != JobStatus.success) {
                throw new RuntimeException("JobStatus was not success");
            }
        });
    }

    private void validateErrorCommandResponse(CommandMessage commandResponse) {
        List<JobStatus> results = commandResponse.getCommandBundles().stream()
                .map(commandBundle ->
                        commandBundle.getCommands().stream()
                                .map(command -> command.getResult().getStatus())
                                .toList())
                .flatMap(List::stream)
                .toList();
        results.forEach(result -> {
            if (result != JobStatus.error) {
                throw new RuntimeException("JobStatus was not success");
            }
        });
        log.info("Received expected error response");
        commandResponse.getCommandBundles().get(0).getCommands().get(0).getResult().getLogs().forEach(tflog -> log.info("{}", tflog));
    }

    private CommandMessage createCommandMessage(String serviceId, String correlationId, String context, String newQueueTfBase64) {
        CommandMessage message = new CommandMessage();
        message.withMessageType(generic);
        message.setMopVer("1");
        message.setMopProtocol(epConfigPush);
        message.setMopMsgType(generic);
        message.setMsgPriority(4);
        message.setMsgUh(MOPUHFlag.ignore);
        message.setContext(context);
        message.setActorId("myActorId");
        message.setOrgId(eventPortalProperties.getOrganizationId());
        message.setTraceId("myTraceId");
        message.setServiceId(serviceId);
        message.setCorrelationId(correlationId);
        message.setCommandBundles(List.of(CommandBundle.builder()
                .executionType(ExecutionType.serial)
                .exitOnFailure(true)
                .commands(
                        List.of(Command.builder()
                                .commandType(CommandType.terraform)
                                .body(newQueueTfBase64)
                                .parameters(Map.of(
                                        "Content-Type", "application/hcl",
                                        "Content-Encoding", "base64"))
                                .command("apply")
                                .build()))
                .build()));
        return message;
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @FunctionalInterface
    public interface Function3Arity<A, B, C> {
        void apply(A a, B b, C c);
    }
}

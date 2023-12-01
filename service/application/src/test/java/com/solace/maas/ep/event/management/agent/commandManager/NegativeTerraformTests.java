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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol.epConfigPush;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@ActiveProfiles("negativetests")
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
    private static final String mainServiceId = "49a23700m80";

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private void setMessageParams(String serviceId, String correlationId, String context, CommandMessage message) {
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
    }

    @Test
    public void createQueuePositiveTest() {
        String correlationId = "myCorrelationId";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);
    }

    @Test
    public void deleteQueuePositiveTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("deleteQueue.tf", mainServiceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadHostTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueueBadHost.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadPortTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueueBadPort.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadCredentialsTest() {
        String correlationId = "myCorrelationId2";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueueBadCredentials.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    // Required to add new user admin2 with R/O privileges
    @Test
    public void createQueueReadOnlyUserTest() {
        String correlationId = "myCorrelationId1";
        String context = "abc123";
        CommandMessage commandResponse = runTest("AddQueueReadOnlyUser.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueSempFailureTest() {

        // First create the queue
        String correlationId = "myCorrelationId";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);

        // Delete the terraform state file
        Path terraformStatePath = Paths.get(terraformProperties.getWorkingDirectoryRoot(),
                context + "-" + mainServiceId, "terraform.tfstate");
        try {
            Files.delete(terraformStatePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Now try to create the queue again
        correlationId = "myCorrelationId2";
        context = "abc123";
        commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badBlockHclFailureTest() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("badBlockHcl.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badFormatHclFailureTest() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("badFormatHcl.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void unknownCommandError() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context, this::createCommandMessageBadCommand);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void sempVersionTooOldTest() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        String oldServiceId = "v0r806w0bmj";
        CommandMessage commandResponse = runTest("addQueue.tf", oldServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badProviderTest() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("badProvider.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badMessagingServiceIdTest() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueue.tf", "imABadServiceId", correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void missingTfEnvVars() {
        String correlationId = "myCorrelationId3";
        String context = "abc123";
        CommandMessage commandResponse = runTest("addQueue.tf", "imABadServiceId", correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }


    @Test
    public void manyRequestsForTheSameContextAtTheSameTime() {
        String context = "abc123";

        String newQueueTf = asString(resourceLoader.getResource("classpath:hcl" + File.separator + "addQueue.tf"));
        String newQueueTfBase64 = Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8));

        int numberOfMessagesToSend = 10;

        AtomicBoolean keepRunning = new AtomicBoolean(true);
        List<CommandMessage> commandResponseList = new CopyOnWriteArrayList<>();
        setupMessageReceiver(commandResponseList, keepRunning, this::handleCommandResponseMessage, numberOfMessagesToSend);

        for (int i = 0; i < numberOfMessagesToSend; i++) {
            log.info("Sending command " + i);
            String correlationId = "myCorrelationId" + i;
            CommandMessage message = createCommandMessageCommand(mainServiceId, correlationId, context, newQueueTfBase64);
            solacePublisher.publish(message,
                    "sc/ep/runtime/" + eventPortalProperties.getOrganizationId() + "/" +
                            eventPortalProperties.getRuntimeAgentId() + "/command/v1/" +
                            correlationId);
        }

        while (keepRunning.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        commandResponseList.forEach(this::validateSuccessCommandResponse);
    }


    private CommandMessage runTest(String hclFileName, String serviceId, String correlationId, String context) {
        return runTest(hclFileName, serviceId, correlationId, context, this::createCommandMessageCommand);
    }

    private CommandMessage runTest(String hclFileName, String serviceId, String correlationId, String context,
                                   Function4ArityWithReturn<String, String, String, String, CommandMessage> createCommandMessage) {

        String newQueueTf = asString(resourceLoader.getResource("classpath:hcl" + File.separator + hclFileName));
        String newQueueTfBase64 = Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8));

        CommandMessage message = createCommandMessage.apply(serviceId, correlationId, context, newQueueTfBase64);

        AtomicBoolean keepRunning = new AtomicBoolean(true);
        List<CommandMessage> commandResponseList = new CopyOnWriteArrayList<>();
        setupMessageReceiver(commandResponseList, keepRunning, this::handleCommandResponseMessage, 1);

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

        return commandResponseList.get(0);
    }

    private void setupMessageReceiver(List<CommandMessage> commandMessageList, AtomicBoolean keepRunning,
                                      Function5ArityVoidReturn<List<CommandMessage>, AtomicBoolean, InboundMessage, Integer, AtomicInteger> messageHandler,
                                      int numberOfExpectedMessages) {
        log.info("TEST: Starting receiver");
        AtomicInteger numberOfMessagesReceived = new AtomicInteger(0);
        DirectMessageReceiver directMessageReceiver = messagingService
                .createDirectMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of("sc/ep/runtime/" +
                        eventPortalProperties.getOrganizationId() + "/" +
                        eventPortalProperties.getRuntimeAgentId() +
                        "/commandResponse/v1/>"))
                .build()
                .start();

        directMessageReceiver.receiveAsync(inboundMessage ->
                messageHandler.apply(commandMessageList, keepRunning, inboundMessage, numberOfExpectedMessages, numberOfMessagesReceived));
    }

    private void handleCommandResponseMessage(List<CommandMessage> commandResponseList, AtomicBoolean keepRunning,
                                              InboundMessage inboundMessage, int numberOfExpectedMessages, AtomicInteger numberOfMessagesReceived) {
        String messageAsString = inboundMessage.getPayloadAsString();
        try {
            CommandMessage receivedCommandMessage = objectMapper.readValue(messageAsString, CommandMessage.class);
            log.info("TEST: Received command response");
            commandResponseList.add(receivedCommandMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            numberOfMessagesReceived.getAndIncrement();
            if (numberOfMessagesReceived.get() >= numberOfExpectedMessages) {
                keepRunning.set(false);
            }
        }
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

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private CommandMessage createCommandMessageBadCommand(String serviceId, String correlationId, String context,
                                                          String newQueueTfBase64) {
        CommandMessage message = new CommandMessage();
        setMessageParams(serviceId, correlationId, context, message);
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
                                .command("blapply")
                                .build()))
                .build()));
        return message;
    }

    private CommandMessage createCommandMessageCommand(String serviceId, String correlationId, String context,
                                                       String newQueueTfBase64) {
        CommandMessage message = new CommandMessage();
        setMessageParams(serviceId, correlationId, context, message);
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

    @FunctionalInterface
    public interface Function5ArityVoidReturn<A, B, C, D, E> {
        void apply(A a, B b, C c, D d, E e);
    }

    @FunctionalInterface
    public interface Function4ArityWithReturn<A, B, C, D, R> {
        R apply(A a, B b, C c, D d);
    }

}

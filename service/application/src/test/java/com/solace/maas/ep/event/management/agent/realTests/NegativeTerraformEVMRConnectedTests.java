package com.solace.maas.ep.event.management.agent.realTests;

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
import com.solace.maas.ep.event.management.agent.realTests.badClasses.BadCommand;
import com.solace.maas.ep.event.management.agent.realTests.badClasses.CommandTypeBad;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.resources.TopicSubscription;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

@SuppressWarnings({"PMD.GodClass", "PMD.UnusedPrivateMethod"})
@Slf4j
@ActiveProfiles("negativetests")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NegativeTerraformEVMRConnectedTests {

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
    private static String context = "abc123";
    private long startTime;

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @BeforeAll
    private static void envVarPrecheck() throws IOException, InterruptedException {
        // These tests require some setup.
        //
        // Make sure the required environment variables are set.
        // "SOLACE_SEMP_PASSWORD", "SOLACE_SEMP_URL", "SOLACE_SEMP_USERNAME" are for a valid Solace broker (version 10.4+)
        // "OLD_SOLACE_SEMP_PASSWORD", "OLD_SOLACE_SEMP_URL", "OLD_SOLACE_SEMP_USERNAME" are for an older Solace broker (version 10.3-)
        // "EP_GATEWAY_MSGVPN", "EP_GATEWAY_PASSWORD", "EP_GATEWAY_URL", "EP_GATEWAY_USERNAME" are for a valid eVMR
        //
        // They can be set for all Junit tests by going to the "Run" menu in IntelliJ, then "Edit Configuration templates", then
        // then "JUnit", and set them in "Environment variables"
        //
        List<String> requiredEnvVars = List.of("EP_GATEWAY_MSGVPN", "EP_GATEWAY_PASSWORD", "EP_GATEWAY_URL", "EP_GATEWAY_USERNAME",
                "EP_ORGANIZATION_ID", "EP_RUNTIME_AGENT_ID", "OLD_SOLACE_SEMP_PASSWORD", "OLD_SOLACE_SEMP_URL", "OLD_SOLACE_SEMP_USERNAME",
                "SOLACE_SEMP_PASSWORD", "SOLACE_SEMP_URL", "SOLACE_SEMP_USERNAME");
        List<String> actualEnvVars = List.copyOf(System.getenv().keySet());
        if (!actualEnvVars.containsAll(requiredEnvVars)) {
            throw new RuntimeException("Missing required environment variables: " + requiredEnvVars);
        }

        // On the valid Solace broker, create a read-only user with username "admin2" and password the same as the "SOLACE_SEMP_PASSWORD" environment variable
        // This is required for the "createQueueReadOnlyUserTest" test
        log.info("Creating user admin2 with R/O privileges");
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"globalAccessLevel\":\"read-only\",\"msgVpnDefaultAccessLevel\":\"none\",\"password\":\"" +
                        System.getenv("SOLACE_SEMP_PASSWORD") + "\",\"userName\":\"admin2\"}"))
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", getBasicAuthenticationHeader(System.getenv("SOLACE_SEMP_USERNAME"),
                        System.getenv("SOLACE_SEMP_PASSWORD")))
                .uri(java.net.URI.create(System.getenv("SOLACE_SEMP_URL") + "/SEMP/v2/__private_config__/usernames"))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }


    @BeforeEach
    private void beforeEach() {
        startTime = System.currentTimeMillis();
    }

    @Test
    public void createQueuePositiveTest() {
        String correlationId = "myCorrelationId";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);
    }

    @Test
    public void deleteQueuePositiveTest() {
        String correlationId = "myCorrelationId2";
        CommandMessage commandResponse = runTest("deleteQueue.tf", mainServiceId, correlationId, context);
        validateSuccessCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadHostTest() {
        testOutputMdTitle2(getTestMethodName());
        String correlationId = "myCorrelationId2";
        CommandMessage commandResponse = runTest("addQueueBadHost.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadPortTest() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId2";
        CommandMessage commandResponse = runTest("addQueueBadPort.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueBadCredentialsTest() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId2";
        CommandMessage commandResponse = runTest("addQueueBadCredentials.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    // Required to add new user admin2 with R/O privileges
    @Test
    public void createQueueReadOnlyUserTest() {
        testOutputMdTitle2(getTestMethodName());
        String correlationId = "myCorrelationId1";
        CommandMessage commandResponse = runTest("AddQueueReadOnlyUser.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void createQueueSempFailureTest() {
        testOutputMdTitle2(getTestMethodName());

        // First create the queue
        String correlationId = "myCorrelationId";
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
        commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badBlockHclFailureTest() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        CommandMessage commandResponse = runTest("badBlockHcl.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badFormatHclFailureTest() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        CommandMessage commandResponse = runTest("badFormatHcl.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void unknownTerraformCommandError() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context, this::createCommandMessageBadTerraformCommand);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void sempVersionTooOldTest() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        String oldServiceId = "v0r806w0bmj";
        CommandMessage commandResponse = runTest("addQueue.tf", oldServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badProviderTest() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        CommandMessage commandResponse = runTest("badProvider.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badMessagingServiceIdTest() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId1";
        CommandMessage commandResponse = runTest("addQueue.tf", "imABadServiceId", correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void badCommandTypeInCommand() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context, this::createCommandMessageBadCommandType);
        validateErrorCommandResponse(commandResponse);
    }


    @Test
    public void missingParametersInCommand() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context, this::createCommandMessageMissingParameters);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void hclCommandIsNotBase64Encoded() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId3";
        CommandMessage commandResponse = runTest("addQueue.tf", mainServiceId, correlationId, context, this::createCommandMessageHclNotBase64Encoded);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void hclHasVariableThatIsNotSetInTerraformPlugin() {
        testOutputMdTitle2(getTestMethodName());

        String correlationId = "myCorrelationId1";
        CommandMessage commandResponse = runTest("addQueueNewVariableNotInPlugin.tf", mainServiceId, correlationId, context);
        validateErrorCommandResponse(commandResponse);
    }

    @Test
    public void manyRequestsForTheSameContextAtTheSameTime() {

        String newQueueTf = asString(resourceLoader.getResource("classpath:hcl" + File.separator + "addQueue.tf"));
        String newQueueTfBase64 = Base64.getEncoder().encodeToString(newQueueTf.getBytes(UTF_8));

        int numberOfMessagesToSend = 10;

        AtomicBoolean keepRunning = new AtomicBoolean(true);
        List<CommandMessage> commandResponseList = new CopyOnWriteArrayList<>();
        setupMessageReceiver(commandResponseList, keepRunning, this::handleCommandResponseMessage, numberOfMessagesToSend);

        for (int i = 0; i < numberOfMessagesToSend; i++) {
            log.debug("Sending command " + i);
            String correlationId = "myCorrelationId" + i;
            CommandMessage message = createCommandMessageCommand(mainServiceId, correlationId, context, newQueueTfBase64);
            solacePublisher.publish(message,
                    "sc/ep/runtime/" + eventPortalProperties.getOrganizationId() + "/" +
                            eventPortalProperties.getRuntimeAgentId() + "/command/v1/" +
                            correlationId);
        }

        waitForReceivedMessages(keepRunning);

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

        log.debug("Sending command");
        solacePublisher.publish(message,
                "sc/ep/runtime/" + eventPortalProperties.getOrganizationId() + "/" +
                        eventPortalProperties.getRuntimeAgentId() + "/command/v1/" +
                        correlationId);

        waitForReceivedMessages(keepRunning);

        return commandResponseList.get(0);
    }

    private static void waitForReceivedMessages(AtomicBoolean keepRunning) {
        while (keepRunning.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setupMessageReceiver(List<CommandMessage> commandMessageList, AtomicBoolean keepRunning,
                                      Function5ArityVoidReturn<List<CommandMessage>, AtomicBoolean, InboundMessage, Integer, AtomicInteger> messageHandler,
                                      int numberOfExpectedMessages) {
        log.debug("TEST: Starting receiver");
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
            log.debug("TEST: Received command response");
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
        log.debug("Received expected error response");
        testOutput("\nTEST OUTPUT:");
        testOutputMDCodeBlock();
        commandResponse.getCommandBundles().get(0).getCommands().get(0).getResult().getLogs()
                .forEach(tflog -> {
                    testOutput(tflog.toString());
                });
        testOutputMDCodeBlock();
        testOutput("Elapsed time (seconds): " + (System.currentTimeMillis() - startTime) / 1000);
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


    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private CommandMessage createCommandMessageMissingParameters(String serviceId, String correlationId, String context,
                                                                 String newQueueTfBase64) {
        CommandMessage commandMessage = createCommandMessageCommand(serviceId, correlationId, context, newQueueTfBase64);
        Command command = commandMessage.getCommandBundles().get(0).getCommands().get(0);
        command.setParameters(Map.of());
        return commandMessage;
    }

    private CommandMessage createCommandMessageBadTerraformCommand(String serviceId, String correlationId, String context,
                                                                   String newQueueTfBase64) {
        CommandMessage commandMessage = createCommandMessageCommand(serviceId, correlationId, context, newQueueTfBase64);
        Command command = commandMessage.getCommandBundles().get(0).getCommands().get(0);
        command.setCommand("badCommand");
        return commandMessage;
    }

    private CommandMessage createCommandMessageHclNotBase64Encoded(String serviceId, String correlationId, String context,
                                                                   String newQueueTfBase64) {
        CommandMessage commandMessage = createCommandMessageCommand(serviceId, correlationId, context, newQueueTfBase64);
        Command command = commandMessage.getCommandBundles().get(0).getCommands().get(0);
        command.setBody("IHaveABadBody");
        return commandMessage;
    }

    private CommandMessage createCommandMessageBadCommandType(String serviceId, String correlationId, String context,
                                                              String newQueueTfBase64) {
        CommandMessage message = new CommandMessage();
        setMessageParams(serviceId, correlationId, context, message);

        BadCommand badCommand = new BadCommand();
        badCommand.setCommandType(CommandTypeBad.badEnum);
        badCommand.setBody(newQueueTfBase64);
        badCommand.setParameters(Map.of(
                "Content-Type", "application/hcl",
                "Content-Encoding", "base64"));
        badCommand.setCommand("apply");

        message.setCommandBundles(List.of(CommandBundle.builder()
                .executionType(ExecutionType.serial)
                .exitOnFailure(true)
                .commands(
                        List.of(badCommand))
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

    private void testOutput(String logMessage) {
        log.info(logMessage);
    }

    private void testOutputMdTitle2(String logMessage) {
        log.info("\n## " + logMessage);
    }

    private void testOutputMDCodeBlock() {
        log.info("```");
    }

    private String getTestMethodName() {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[2].getMethodName();
    }
}

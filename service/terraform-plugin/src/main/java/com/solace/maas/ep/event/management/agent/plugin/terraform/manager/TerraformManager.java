package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClientFactory;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TerraformManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final TerraformLogProcessingService terraformLogProcessingService;
    private final TerraformProperties terraformProperties;
    private final TerraformClientFactory terraformClientFactory;

    public TerraformManager(TerraformLogProcessingService terraformLogProcessingService,
                            TerraformProperties terraformProperties, TerraformClientFactory terraformClientFactory) {
        this.terraformLogProcessingService = terraformLogProcessingService;
        this.terraformProperties = terraformProperties;
        this.terraformClientFactory = terraformClientFactory;
    }

    public void execute(CommandRequest request, Command command, Map<String, String> envVars) {

        MDC.put(RouteConstants.COMMAND_CORRELATION_ID, request.getCommandCorrelationId());
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID, request.getServiceId());

        log.debug("Executing command {} for serviceId {} correlationId {} context {}", command.getCommand(), request.getServiceId(),
                request.getCommandCorrelationId(), request.getContext());

        setEnvVarsFromParameters(command, envVars);

        try (TerraformClient terraformClient = terraformClientFactory.createClient()) {

            Path configPath = TerraformUtils.createConfigPath(request, terraformProperties.getWorkingDirectoryRoot());
            String commandVerb = command.getCommand();
            List<String> logOutput = executeTerraformCommand(command, envVars, configPath, terraformClient);
            processTerraformResponse(command, commandVerb, logOutput);
        } catch (InterruptedException e) {
            log.error("Received a thread interrupt while executing the terraform command", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("An error was encountered while executing the terraform command", e);
            TerraformUtils.setCommandError(command, e);
        }
    }

    private static void setEnvVarsFromParameters(Command command, Map<String, String> envVars) {
        if (command.getParameters() != null && command.getParameters().containsKey("environment") &&
                command.getParameters().get("environment") instanceof Map) {
            Map<String, String> environmentMap = (Map<String, String>) command.getParameters().get("environment");
            environmentMap.keySet().forEach(key -> envVars.put("TF_VAR_" + key, environmentMap.get(key)));
        }
    }

    private List<String> executeTerraformCommand(Command command, Map<String, String> envVars, Path configPath,
                                                 TerraformClient terraformClient) throws IOException, InterruptedException, ExecutionException {
        String commandVerb = command.getCommand();

        Consumer<String> logToConsole = (tfLog) -> logToConsole(tfLog);
        if ("import".equals(commandVerb)) {
            logToConsole = (log) -> {
            };
        }
        List<String> logOutput = TerraformUtils.setupTerraformClient(terraformClient, configPath, logToConsole);

        switch (commandVerb) {
            case "import" -> importCommand(envVars, configPath, terraformClient, logOutput);
            case "apply" -> {
                TerraformUtils.writeHclToFile(command, configPath);
                terraformClient.apply(envVars).get();
            }
            case "write_HCL" -> TerraformUtils.writeHclToFile(command, configPath);
            default -> throw new IllegalArgumentException("Unsupported command " + commandVerb);
        }
        return logOutput;
    }

    private static void importCommand(Map<String, String> envVars, Path configPath, TerraformClient terraformClient, List<String> output) throws InterruptedException, ExecutionException, IOException {
        boolean importPlanSuccessful = terraformClient.plan(envVars).get();

        if (!importPlanSuccessful) {
            // Re-write the import file to only include the successful imports
            String successfulImports = output.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, Map.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(log -> "planned_change".equals(log.get("type")))
                    .map(log -> (Map<String, Object>) log.get("change"))
                    .filter(log -> "import".equals(log.get("action")))
                    .map(log -> new TerraformImport(
                            ((Map<String, Object>) log.get("importing")).get("id").toString(),
                            ((Map<String, Object>) log.get("resource")).get("resource").toString()))
                    .map(TerraformImport::toString)
                    .collect(Collectors.joining("\n"));

            Files.writeString(configPath.resolve("import.tf"), successfulImports);
        }
    }

    private void processTerraformResponse(Command command, String commandVerb, List<String> output) {
        // Process logs and create the result
        if (Boolean.TRUE.equals(command.getIgnoreResult())) {
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .logs(List.of())
                    .build());
        } else {
            if (!"write_HCL".equals(commandVerb)) {
                command.setResult(terraformLogProcessingService.buildTfCommandResult(output));
            } else {
                command.setResult(CommandResult.builder()
                        .status(JobStatus.success)
                        .logs(List.of())
                        .build());
            }
        }
    }

    @SneakyThrows
    public void logToConsole(String tfLog) {

        String logMessage = String.format("Terraform output: %s", tfLog);

        Map<String, Object> logMop = objectMapper.readValue(tfLog, Map.class);
        String logLevel = (String) logMop.get("@level");
        switch (logLevel) {
            case "trace" -> log.trace(logMessage);
            case "debug" -> log.debug(logMessage);
            case "info" -> log.info(logMessage);
            case "warn" -> log.warn(logMessage);
            case "error" -> log.error(logMessage);
            default -> log.error("cannot map the logLevel properly for tfLog {}", tfLog);
        }
    }

}

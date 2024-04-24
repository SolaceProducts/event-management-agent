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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
    public static final String SYNC_HCL_FILENAME = "sync.tf";
    public static final String ENVIRONMENT_KEY = "environment";
    public static final String EXECUTION_LOG_FILE = "commandExecution.log";
    private final TerraformLogProcessingService terraformLogProcessingService;
    private final TerraformProperties terraformProperties;
    private final TerraformClientFactory terraformClientFactory;

    public TerraformManager(TerraformLogProcessingService terraformLogProcessingService,
                            TerraformProperties terraformProperties,
                            TerraformClientFactory terraformClientFactory) {
        this.terraformLogProcessingService = terraformLogProcessingService;
        this.terraformProperties = terraformProperties;
        this.terraformClientFactory = terraformClientFactory;
    }

    public Path execute(CommandRequest request, Command command, Map<String, String> envVars) {
        MDC.put(RouteConstants.COMMAND_CORRELATION_ID, request.getCommandCorrelationId());
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID, request.getServiceId());
        log.debug("Executing command {} for serviceId {} correlationId {} context {}", command.getCommand(), request.getServiceId(),
                request.getCommandCorrelationId(), request.getContext());
        setEnvVarsFromParameters(command, envVars);
        PrintWriter executionLogWriter = null;
        Path executionLogFilePath = null;
        try (TerraformClient terraformClient = terraformClientFactory.createClient()) {
            Path configPath = TerraformUtils.createConfigPath(request, terraformProperties.getWorkingDirectoryRoot());
            Path executionLogPath = TerraformUtils.createCommandExecutionLogDir(configPath);
            executionLogFilePath = executionLogPath.resolve(command.getCommand() + "-" + EXECUTION_LOG_FILE);
            executionLogWriter = new PrintWriter(new FileOutputStream(executionLogFilePath.toString(), false), true);
            String commandVerb = command.getCommand();
            List<String> logOutput = executeTerraformCommand(
                    command,
                    envVars,
                    configPath,
                    terraformClient,
                    executionLogWriter
            );
            processTerraformResponse(command, commandVerb, logOutput);
        } catch (InterruptedException e) {
            log.error("Received a thread interrupt while executing the terraform command", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("An error was encountered while executing the terraform command", e);
            TerraformUtils.setCommandError(command, e);
        } finally {
            if (executionLogWriter != null) {
                executionLogWriter.close();
            }
        }
        return executionLogFilePath;
    }

    private static void setEnvVarsFromParameters(Command command, Map<String, String> envVars) {
        if (command.getParameters() != null && command.getParameters().containsKey(ENVIRONMENT_KEY) &&
                command.getParameters().get(ENVIRONMENT_KEY) instanceof Map) {
            Map<String, String> environmentMap = (Map<String, String>) command.getParameters().get(ENVIRONMENT_KEY);
            environmentMap.keySet().forEach(key -> envVars.put("TF_VAR_" + key, environmentMap.get(key)));
        }
    }

    private List<String> executeTerraformCommand(Command command,
                                                 Map<String, String> envVars,
                                                 Path configPath,
                                                 TerraformClient terraformClient,
                                                 PrintWriter writer) throws IOException, InterruptedException, ExecutionException {
        String commandVerb = command.getCommand();

        Consumer<String> logToConsole = this::logToConsole;
        Consumer<String> logToFile = s -> writeToExecutionLogFile(s, writer);
        if ("sync".equals(commandVerb)) {
            logToConsole = log -> {
            };
            logToFile = log -> {
            };
        }
        List<String> logOutput = TerraformUtils.setupTerraformClient(
                terraformClient,
                configPath,
                logToConsole,
                logToFile
        );

        switch (commandVerb) {
            case "sync" -> syncCommand(envVars, configPath, terraformClient, logOutput);
            case "apply" -> {
                TerraformUtils.writeHclToFile(command, configPath, writer, objectMapper);
                terraformClient.apply(envVars).get();
            }
            case "write_HCL" -> TerraformUtils.writeHclToFile(command, configPath, writer, objectMapper);
            default -> throw new IllegalArgumentException("Unsupported command " + commandVerb);
        }
        return logOutput;
    }

    private static void syncCommand(Map<String, String> envVars, Path configPath, TerraformClient terraformClient, List<String> output) throws InterruptedException, ExecutionException, IOException {
        boolean syncPlanSuccessful = terraformClient.plan(envVars).get();

        if (!syncPlanSuccessful) {
            // Re-write the sync file to only include the successful imports
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

            Files.writeString(configPath.resolve(SYNC_HCL_FILENAME), successfulImports);
        }
    }

    private void processTerraformResponse(Command command, String commandVerb, List<String> output) {
        // Process logs and create the result
        if (Boolean.FALSE.equals(command.getIgnoreResult())) {
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

    @SneakyThrows
    public void writeToExecutionLogFile(String tfLog, PrintWriter writer) {
        writer.println(tfLog);
    }

}

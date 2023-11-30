package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClientFactory;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;

@Service
@Slf4j
public class TerraformManager {
    public static final String LOG_LEVEL_ERROR = "ERROR";
    private final TerraformLogProcessingService terraformLogProcessingService;
    private final TerraformProperties terraformProperties;
    private final TerraformClientFactory terraformClientFactory;

    private final static String TF_CONFIG_FILENAME = "config.tf";

    public TerraformManager(TerraformLogProcessingService terraformLogProcessingService,
                            TerraformProperties terraformProperties, TerraformClientFactory terraformClientFactory) {
        this.terraformLogProcessingService = terraformLogProcessingService;
        this.terraformProperties = terraformProperties;
        this.terraformClientFactory = terraformClientFactory;
    }

    public void execute(CommandRequest request, Command command, Map<String, String> envVars) {

        MDC.put(COMMAND_CORRELATION_ID, request.getCorrelationId());
        MDC.put(MESSAGING_SERVICE_ID, request.getServiceId());
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");

        log.debug("Executing command {} for serviceId {} correlationId {} context {}", command.getCommand(), request.getServiceId(),
                request.getCorrelationId(), request.getContext());

        try (TerraformClient terraformClient = terraformClientFactory.createClient()) {

            Path configPath = createConfigPath(request);
            List<String> logOutput = setupTerraformClient(terraformClient, configPath, traceId, spanId);
            String commandVerb = executeTerraformCommand(command, envVars, configPath, terraformClient);
            processTerraformResponse(request, command, commandVerb, logOutput);
        } catch (InterruptedException e) {
            log.error("Received a thread interrupt while executing the terraform command", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("An error was encountered while executing the terraform command", e);
            setCommandError(command, e);
        }
    }

    private static List<String> setupTerraformClient(TerraformClient terraformClient, Path configPath, String traceId, String spanId) {
        terraformClient.setWorkingDirectory(configPath.toFile());
        List<String> output = new ArrayList<>();

        // Write each terraform to the output list so that it can be processed later
        // Also write the output to the main log to be streamed back to EP
        terraformClient.setOutputListener(tfLog -> {
            MDC.put("traceId", traceId);
            MDC.put("spanId", spanId);
            output.add(tfLog);
            log.debug("Terraform output: {}", tfLog);
        });
        return output;
    }

    private static String executeTerraformCommand(Command command, Map<String, String> envVars, Path configPath, TerraformClient terraformClient) throws IOException, InterruptedException, ExecutionException {
        String commandVerb = command.getCommand();
        switch (commandVerb) {
            case "apply" -> {
                writeHclToFile(command, configPath);
                Boolean planSuccessful = terraformClient.plan(envVars).get();
                if (Boolean.TRUE.equals(planSuccessful)) {
                    terraformClient.apply(envVars).get();
                }
            }
            case "write_HCL" -> writeHclToFile(command, configPath);
            default -> throw new IllegalArgumentException("Unsupported command " + commandVerb);
        }
        return commandVerb;
    }

    private void processTerraformResponse(CommandRequest request, Command command, String commandVerb, List<String> output) throws IOException {
        // Process logs and create the result
        if (Boolean.TRUE.equals(command.getIgnoreResult())) {
            command.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .logs(List.of())
                    .build());
        } else {
            if (!"write_HCL".equals(commandVerb)) {
                terraformLogProcessingService.saveLogToFile(request, output);
                command.setResult(terraformLogProcessingService.buildTfCommandResult(output));
            } else {
                command.setResult(CommandResult.builder()
                        .status(JobStatus.success)
                        .logs(List.of())
                        .build());
            }
        }
    }

    private void setCommandError(Command command, Exception e) {
        command.setResult(CommandResult.builder()
                .status(JobStatus.error)
                .logs(List.of(
                        Map.of("message", e.getMessage(),
                                "errorType", e.getClass().getName(),
                                "level", LOG_LEVEL_ERROR,
                                "timestamp", OffsetDateTime.now())))
                .build());
    }

    private Path createConfigPath(CommandRequest request) {
        Path configPath = Paths.get(terraformProperties.getWorkingDirectoryRoot() + File.separator
                + request.getContext()
                + "-"
                + request.getServiceId()
                + File.separator
        );

        if (Files.notExists(configPath)) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return configPath;
    }

    private static void writeHclToFile(Command command, Path configPath) throws IOException {
        if (StringUtils.isNotEmpty(command.getBody())) {
            // At the moment, we only support base64 decoding
            Map<String, String> parameters = command.getParameters();
            if (parameters != null && parameters.containsKey("Content-Encoding") && "base64".equals(parameters.get("Content-Encoding"))) {
                byte[] decodedBytes = Base64.getDecoder().decode(command.getBody());
                Files.write(configPath.resolve(TF_CONFIG_FILENAME), decodedBytes);
            } else {
                if (parameters == null || !parameters.containsKey("Content-Encoding")) {
                    throw new IllegalArgumentException("Missing Content-Encoding property in command parameters.");
                }

                throw new IllegalArgumentException("Unsupported encoding type " + parameters.get("Content-Encoding"));
            }
        }
    }
}

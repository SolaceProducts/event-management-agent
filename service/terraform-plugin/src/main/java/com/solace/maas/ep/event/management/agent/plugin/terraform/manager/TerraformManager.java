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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.COMMAND_CORRELATION_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;

@Service
@Slf4j
public class TerraformManager {
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
        MDC.put(MESSAGING_SERVICE_ID, request.getMessagingServiceId());
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");

        log.debug("Executing command {} for ms {} correlationId {} context {}", command.getCommand(), request.getMessagingServiceId(),
                request.getCorrelationId(), request.getContext());

        try (TerraformClient terraformClient = terraformClientFactory.createClient()) {

            Path configPath = createConfigPath(request);
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
            String commandVerb = command.getCommand();
            try {
                switch (commandVerb) {
                    case "apply" -> {
                        writeHclToFile(command, configPath);
                        terraformClient.plan(envVars).get();
                        terraformClient.apply(envVars).get();
                    }
                    case "write_HCL" -> writeHclToFile(command, configPath);
                    default -> log.error("Cannot handle arbitrary commands.");
                }

                // Process logs and create the result
                if (Boolean.TRUE.equals(command.getIgnoreResult())) {
                    command.setResult(CommandResult.builder()
                            .status(JobStatus.success)
                            .logs(List.of())
                            .errors(List.of())
                            .build());
                } else {
                    if (!"write_HCL".equals(commandVerb)) {
                        terraformLogProcessingService.saveLogToFile(request, output);
                        command.setResult(terraformLogProcessingService.buildTfCommandResult(output));
                    } else {
                        command.setResult(CommandResult.builder()
                                .status(JobStatus.success)
                                .logs(List.of())
                                .errors(List.of())
                                .build());
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Path createConfigPath(CommandRequest request) {
        Path configPath = Paths.get(terraformProperties.getWorkingDirectoryRoot() + File.separator
                + request.getContext()
                + "-"
                + request.getMessagingServiceId()
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
            byte[] decodedBytes = Base64.getDecoder().decode(command.getBody());
            Files.write(configPath.resolve(TF_CONFIG_FILENAME), decodedBytes);
        }
    }
}

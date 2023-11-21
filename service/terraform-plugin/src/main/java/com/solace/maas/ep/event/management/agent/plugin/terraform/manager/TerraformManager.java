package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
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
    private final TerraformClient terraformClient;
    private final TerraformLogProcessingService terraformLogProcessingService;

    @Value("${plugins.terraform.workingDirectoryRoot:/${HOME}/config}")
    private String workingDirectoryRoot;

    private final static String TF_CONFIG_FILENAME = "config.tf";

    public TerraformManager(TerraformClient terraformClient, TerraformLogProcessingService terraformLogProcessingService) {
        this.terraformClient = terraformClient;
        this.terraformLogProcessingService = terraformLogProcessingService;
    }

    public void execute(CommandRequest request, Command command, Map<String, String> envVars) {

        MDC.put(COMMAND_CORRELATION_ID, request.getCorrelationId());
        MDC.put(MESSAGING_SERVICE_ID, request.getMessagingServiceId());

        log.debug("Executing command {} for ms {} correlationId {} context {}", command, request.getMessagingServiceId(),
                request.getCorrelationId(), request.getContext());

        Path configPath = Paths.get(workingDirectoryRoot + File.separator
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
        terraformClient.setWorkingDirectory(configPath.toFile());
        List<String> output = new ArrayList<>();
        // The output is a series of individual JSON objects
        // We collect them into lists and eventually concatenate them into a single valid JSON list
        terraformClient.setOutputListener(output::add);
        String commandVerb = command.getCommand();
        try {
            switch (commandVerb) {
                case "plan" -> {
                    writeHclToFile(command, configPath);
                    terraformClient.plan(envVars).get();
                }
                case "apply" -> {
                    writeHclToFile(command, configPath);
                    terraformClient.plan(envVars).get();
                    terraformClient.apply(envVars).get();
                }
                case "import" -> {
                    writeHclToFile(command, configPath);
                    String address = command.getParameters().get("address");
                    String tfId = command.getParameters().get("tfId");
                    terraformClient.importCommand(envVars, address, tfId).get();
                }
                case "write_HCL" -> writeHclToFile(command, configPath);
                default -> log.error("Cannot handle arbitrary commands.");
            }

            // Process logs and create the result
            //TODO: We may need to have a log rotation/cleaning mechanism
            if (Boolean.TRUE.equals(command.getIgnoreResult())) {
                command.setResult(CommandResult.builder()
                        .status(JobStatus.success)
                        .build());
            } else {
                if (!"write_HCL".equals(commandVerb)) {
                    terraformLogProcessingService.saveLogToFile(request, output);

                    if (commandVerb.equals("import")) {
                        setOutputForImportCommand(command, output);
                    } else {
                        command.setResult(terraformLogProcessingService.buildTfCommandResult(output));
                    }
                } else {
                    command.setResult(CommandResult.builder()
                            .status(JobStatus.success)
                            .build());
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void setOutputForImportCommand(Command command, List<String> output) {
        command.setResult(CommandResult.builder()
                .status(JobStatus.success)
                .logs(List.of(
                        Map.of("address", command.getParameters().get("address"),
                                "message", output)))
                .errors(List.of())
                .build());
    }

    private static void writeHclToFile(Command command, Path configPath) throws IOException {
        if (StringUtils.isNotEmpty(command.getBody())) {
            byte[] decodedBytes = Base64.getDecoder().decode(command.getBody());
            Files.write(configPath.resolve(TF_CONFIG_FILENAME), decodedBytes);
        }
    }

    private static void deleteHclFile(Path configPath) throws IOException {
        Path filePath = configPath.resolve(TF_CONFIG_FILENAME);

        boolean isDeleted = Files.deleteIfExists(filePath);
        if (isDeleted) {
            log.info("File deleted successfully.");
        } else {
            log.error("File does not exist or could not be deleted.");
        }
    }
}

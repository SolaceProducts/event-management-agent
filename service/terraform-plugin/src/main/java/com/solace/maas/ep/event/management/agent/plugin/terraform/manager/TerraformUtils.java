package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import org.apache.commons.lang.StringUtils;

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
import java.util.function.Consumer;

public class TerraformUtils {
    public static final String LOG_LEVEL_ERROR = "ERROR";
    private static final String DEFAULT_TF_CONFIG_FILENAME = "config.tf";

    public static List<String> setupTerraformClient(TerraformClient terraformClient, Path configPath,
                                                    Consumer<String> logToConsole) {
        terraformClient.setWorkingDirectory(configPath.toFile());
        List<String> output = new ArrayList<>();

        // Write each terraform to the output list so that it can be processed later
        // Also write the output to the main log to be streamed back to EP
        terraformClient.setOutputListener(tfLog -> {
            output.add(tfLog);
            logToConsole.accept(tfLog);
        });
        return output;
    }

    public static void setCommandError(Command command, Exception e) {
        command.setResult(CommandResult.builder()
                .status(JobStatus.error)
                .logs(List.of(
                        Map.of("message", e.getMessage(),
                                "errorType", e.getClass().getName(),
                                "level", LOG_LEVEL_ERROR,
                                "timestamp", OffsetDateTime.now())))
                .build());
    }

    public static Path createConfigPath(CommandRequest request, String directory) {
        Path configPath = Paths.get(directory + File.separator
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

    public static void writeHclToFile(Command command, Path configPath) throws IOException {
        if (StringUtils.isNotEmpty(command.getBody())) {
            // At the moment, we only support base64 decoding
            Map<String, Object> parameters = command.getParameters();
            if (parameters != null && parameters.containsKey("Content-Encoding") && "base64".equals(parameters.get("Content-Encoding"))) {
                byte[] decodedBytes;
                try {
                    decodedBytes = Base64.getDecoder().decode(command.getBody());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Error decoding base64 content", e);
                }
                String filename = DEFAULT_TF_CONFIG_FILENAME;
                if (parameters.containsKey("Output-File-Name")) {
                    filename = (String) parameters.get("Output-File-Name");
                }
                Files.write(configPath.resolve(filename), decodedBytes);
            } else {
                if (parameters == null || !parameters.containsKey("Content-Encoding")) {
                    throw new IllegalArgumentException("Missing Content-Encoding property in command parameters.");
                }

                throw new IllegalArgumentException("Unsupported encoding type " + parameters.get("Content-Encoding"));
            }
        }
    }

}

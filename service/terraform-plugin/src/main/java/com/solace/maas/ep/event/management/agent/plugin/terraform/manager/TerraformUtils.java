package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.logging.LogLevel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

public class TerraformUtils {
    public static final String LOG_LEVEL_ERROR = "ERROR";
    private static final String DEFAULT_TF_CONFIG_FILENAME = "config.tf";
    public static final String CONTENT_ENCODING = "Content-Encoding";

    private TerraformUtils() {
    }

    public static List<String> setupTerraformClient(TerraformClient terraformClient,
                                                    Path configPath,
                                                    Consumer<String> logToConsole,
                                                    Consumer<String> logToFile) {
        terraformClient.setWorkingDirectory(configPath.toFile());
        List<String> output = new ArrayList<>();

        // Write each terraform to the output list so that it can be processed later
        // Also write the output to the main log to be streamed back to EP
        terraformClient.setOutputListener(tfLog -> {
            output.add(tfLog);
            logToConsole.accept(tfLog);
            logToFile.accept(tfLog);
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

    public static void deleteConfigPath(CommandRequest request, String directory) {
        Path configPath = Paths.get(directory + File.separator
                + request.getContext()
                + "-"
                + request.getServiceId()
                + File.separator
        );

        if (Files.exists(configPath)) {
            try {
                deleteDirectory(configPath);
            } catch (IOException e) {
                throw new IllegalStateException("Failed removing Terraform state directory: "+directory,e);
            }
        }
    }

    private static void deleteDirectory(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
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

    public static Path createCommandExecutionLogDir(Path root) {
        Path ececutionLogPath = root.resolve("executionLogs");
        if (Files.notExists(ececutionLogPath)) {
            try {
                Files.createDirectories(ececutionLogPath);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return ececutionLogPath;
    }

    public static void writeHclToFile(Command command,
                                      Path configPath,
                                      PrintWriter writer,
                                      ObjectMapper objectMapper) {
        try {
            if (StringUtils.isNotEmpty(command.getBody())) {
                // At the moment, we only support base64 decoding
                Map<String, Object> parameters = command.getParameters();
                if (parameters != null && parameters.containsKey(CONTENT_ENCODING) && "base64".equals(parameters.get(CONTENT_ENCODING))) {
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
                    if (parameters == null || !parameters.containsKey(CONTENT_ENCODING)) {
                        throw new IllegalArgumentException("Missing Content-Encoding property in command parameters.");
                    }

                    throw new IllegalArgumentException("Unsupported encoding type " + parameters.get(CONTENT_ENCODING));
                }
            }
        } catch (Exception e) {
            writer.println(
                    convertGenericLogMessageToTFStyleMessage(
                            String.format(
                                    "Error while writing HCL file to disk. Error message: %s",
                                    e.getMessage()
                            ), TerraformLogProcessingService.VALUE_LOG_LEVEL_ERROR, objectMapper)
            );
            throw new IllegalArgumentException(e);
        }
    }

    public static String convertGenericLogMessageToTFStyleMessage(String genericMessage,
                                                                  String logLevel,
                                                                  ObjectMapper objectMapper) {

        if (StringUtils.isEmpty(logLevel)) {
            throw new UnsupportedOperationException("Unsupported log level " + logLevel);
        }

        try {
            return objectMapper.writeValueAsString(Map.of(
                    TerraformLogProcessingService.KEY_LOG_LEVEL, logLevel.toLowerCase(),
                    TerraformLogProcessingService.KEY_MESSAGE, genericMessage,
                    TerraformLogProcessingService.KEY_TIMESTAMP, Instant.now().toString()
            ));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
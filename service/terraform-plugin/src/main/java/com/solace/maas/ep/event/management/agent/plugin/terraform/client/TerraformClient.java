package com.solace.maas.ep.event.management.agent.plugin.terraform.client;

/**
 * This file is a modification of the microsoft sprint-boot-terraform client found
 * at: https://github.com/microsoft/terraform-spring-boot
 * <p>
 * The following is a copy of the license to comply with the terms of use.
 *
 * <p>
 * <p>
 * MIT License
 * <p>
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@ExcludeFromJacocoGeneratedReport
public class TerraformClient implements AutoCloseable {
    private static final String TERRAFORM_EXE_NAME = "terraform";
    private static final String VERSION_COMMAND = "version",
            PLAN_COMMAND = "plan",
            APPLY_COMMAND = "apply",
            DESTROY_COMMAND = "destroy",
            IMPORT_COMMAND = "import";
    private static final Map<String, List<String>> NON_INTERACTIVE_COMMAND_MAP = new HashMap<>();

    static {
        NON_INTERACTIVE_COMMAND_MAP.put(APPLY_COMMAND, List.of("-json", "-auto-approve"));
        NON_INTERACTIVE_COMMAND_MAP.put(PLAN_COMMAND, List.of("-json"));
        NON_INTERACTIVE_COMMAND_MAP.put(DESTROY_COMMAND, List.of("-json", "-auto-approve"));
    }

    private final ExecutorService executor = Executors.newWorkStealingPool();
    private File workingDirectory;
    private boolean inheritIO;
    private Consumer<String> outputListener, errorListener;

    public Consumer<String> getOutputListener() {
        return outputListener;
    }

    public void setOutputListener(Consumer<String> listener) {
        outputListener = listener;
    }

    public Consumer<String> getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(Consumer<String> listener) {
        errorListener = listener;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void setWorkingDirectory(Path folderPath) {
        setWorkingDirectory(folderPath.toFile());
    }

    public boolean isInheritIO() {
        return inheritIO;
    }

    public void setInheritIO(boolean inheritIO) {
        this.inheritIO = inheritIO;
    }

    public CompletableFuture<String> version() throws IOException {
        ProcessLauncher launcher = getTerraformLauncher(VERSION_COMMAND, Map.of());
        StringBuilder version = new StringBuilder();
        Consumer<String> outputListener = getOutputListener();
        launcher.setOutputListener(m -> {
            version.append(version.length() == 0 ? m : "");
            if (outputListener != null) {
                outputListener.accept(m);
            }
        });
        return launcher.launch().thenApply((c) -> c == 0 ? version.toString() : null);
    }

    public CompletableFuture<Boolean> plan(Map<String, String> envVars) throws IOException {
        checkRunningParameters();
        return run(envVars, PLAN_COMMAND);
    }

    public CompletableFuture<Boolean> apply(Map<String, String> envVars) throws IOException {
        checkRunningParameters();
        return run(envVars, APPLY_COMMAND);
    }

    public CompletableFuture<Boolean> importCommand(Map<String, String> envVars, String address, String tfId) throws IOException {
        checkRunningParameters();
        return run(envVars, IMPORT_COMMAND + " -no-color " + address + " " + tfId);
    }

    public CompletableFuture<Boolean> destroy() throws IOException {
        checkRunningParameters();
        return run(DESTROY_COMMAND);
    }

    private CompletableFuture<Boolean> run(String... commands) throws IOException {
        return run(Map.of(), commands);
    }

    private CompletableFuture<Boolean> run(Map<String, String> envVars, String... commands) throws IOException {
        assert commands.length > 0;
        ProcessLauncher[] launchers = new ProcessLauncher[commands.length];
        for (int i = 0; i < launchers.length; i++) {
            launchers[i] = getTerraformLauncher(commands[i], envVars);
        }

        CompletableFuture<Integer> result = launchers[0].launch().thenApply(c -> c == 0 ? 1 : -1);
        for (int i = 1; i < commands.length; i++) {
            result = result.thenCompose(index -> {
                if (index > 0) {
                    return launchers[index].launch().thenApply(c -> c == 0 ? index + 1 : -1);
                }
                return CompletableFuture.completedFuture(-1);
            });
        }
        return result.thenApply(i -> i > 0);
    }

    private void checkRunningParameters() {
        if (getWorkingDirectory() == null) {
            throw new IllegalArgumentException("working directory should not be null");
        }
    }

    private ProcessLauncher getTerraformLauncher(String command, Map<String, String> envVars) {
        ProcessLauncher launcher = new ProcessLauncher(executor, addToBeginningOfArray(TERRAFORM_EXE_NAME, command.split(" ")));
        launcher.setDirectory(getWorkingDirectory());
        launcher.setInheritIO(isInheritIO());
        launcher.appendCommands(
                Optional.ofNullable(NON_INTERACTIVE_COMMAND_MAP.get(command))
                        .orElse(List.of())
                        .toArray(String[]::new));
        launcher.setOutputListener(getOutputListener());
        launcher.setErrorListener(getErrorListener());
        envVars.keySet().stream()
                .forEach(key -> launcher.setEnvironmentVariable(key, envVars.get(key)));
        return launcher;
    }

    @Override
    public void close() throws Exception {
        executor.shutdownNow();
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("executor did not terminate");
        }
    }

    private String[] addToBeginningOfArray(String elementToAdd, String[] originalArray) {
        String[] newArray = new String[originalArray.length + 1];
        newArray[0] = elementToAdd;
        System.arraycopy(originalArray, 0, newArray, 1, originalArray.length);
        return newArray;
    }
}

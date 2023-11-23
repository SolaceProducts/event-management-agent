package com.solace.maas.ep.event.management.agent.plugin.terraform.client;

/**
 * This file is a modification of the microsoft sprint-boot-terraform client found
 * at: https://github.com/microsoft/terraform-spring-boot
 * <p>
 * The following is a copy of the license to comply with the MIT license terms of use.
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
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExcludeFromJacocoGeneratedReport
@Slf4j
final class ProcessLauncher {
    private Process process;
    private final ProcessBuilder builder;
    private Consumer<String> outputListener, errorListener;
    private boolean inheritIO;
    private final ExecutorService executor;

    ProcessLauncher(ExecutorService executor, String... commands) {
        assert executor != null;
        this.executor = executor;
        process = null;
        builder = new ProcessBuilder(commands);
    }

    void setOutputListener(Consumer<String> listener) {
        assert process == null;
        outputListener = listener;
    }

    void setErrorListener(Consumer<String> listener) {
        assert process == null;
        errorListener = listener;
    }

    void setInheritIO(boolean inheritIO) {
        assert process == null;
        this.inheritIO = inheritIO;
    }

    void setDirectory(File directory) {
        assert process == null;
        builder.directory(directory);
    }

    void appendCommands(String... commands) {
        Stream<String> filteredCommands = Arrays.stream(commands).filter(c -> c != null && c.length() > 0);
        builder.command().addAll(filteredCommands.collect(Collectors.toList()));
    }

    void setEnvironmentVariable(String name, String value) {
        assert name != null && name.length() > 0;
        Map<String, String> env = builder.environment();
        value = (value != null ? env.put(name, value) : env.remove(name));
    }

    void setOrAppendEnvironmentVariable(String name, String value, String delimiter) {
        assert name != null && name.length() > 0;
        if (value != null && value.length() > 0) {
            String current = System.getenv(name);
            String target = (current == null || current.length() == 0 ? value : String.join(delimiter, current, value));
            setEnvironmentVariable(name, target);
        }
    }

    CompletableFuture<Integer> launch() {
        assert process == null;
        if (inheritIO) {
            builder.inheritIO();
        }

        log.debug("Executing command: " + String.join(" ", builder.command()));

        try {
            process = builder.start();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (!inheritIO) {
            if (outputListener != null) {
                executor.submit(() -> readProcessStream(process.getInputStream(), outputListener));
            }
            if (errorListener != null) {
                executor.submit(() -> readProcessStream(process.getErrorStream(), errorListener));
            }
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                return process.waitFor();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }, executor);
    }

    private boolean readProcessStream(InputStream stream, Consumer<String> listener) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    listener.accept(line);
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}

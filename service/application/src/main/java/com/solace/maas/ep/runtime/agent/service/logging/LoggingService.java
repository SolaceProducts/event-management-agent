package com.solace.maas.ep.runtime.agent.service.logging;

import com.solace.maas.ep.runtime.agent.logging.FileLoggerFactory;
import com.solace.maas.ep.runtime.agent.processor.LoggingProcessor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.SCAN_ID;

@Slf4j
@Service
public class LoggingService {
    private final Map<String, List<LoggingProcessor>> logStore = new HashMap<>();

    private final FileLoggerFactory fileLoggerFactory;

    public LoggingService(FileLoggerFactory fileLoggerFactory) {
        this.fileLoggerFactory = fileLoggerFactory;
    }

    public void addLoggingProcessor(String scanId, LoggingProcessor loggingProcessor) {
        if (logStore.containsKey(scanId)) {
            List<LoggingProcessor> processors = logStore.get(scanId);
            if (!processors.contains(loggingProcessor)) {
                processors.add(loggingProcessor);
                logStore.put(scanId, processors);
            }
        } else {
            List<LoggingProcessor> processors = new ArrayList<>();
            processors.add(loggingProcessor);
            logStore.put(scanId, processors);
        }
    }

    public void removeLoggingProcessor(String scanId) {
        logStore.remove(scanId);
    }

    public boolean hasLoggingProcessor(String scanId) {
        return logStore.containsKey(scanId);
    }

    public List<LoggingProcessor> getLoggingProcessor(String scanId) {
        return logStore.get(scanId);
    }

    public void createScanFileLogger(String scanId) {
        fileLoggerFactory.create();
        LoggingProcessor loggingProcessor = new LoggingProcessor("FileAppender");
        addLoggingProcessor(scanId, loggingProcessor);
    }

    public void prepareLoggers(String scanId) {
        if (!hasLoggingProcessor(scanId)) {
            MDC.put(SCAN_ID, scanId);
            createScanFileLogger(scanId);
        }
    }
}

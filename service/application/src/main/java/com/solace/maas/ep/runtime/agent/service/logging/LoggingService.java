package com.solace.maas.ep.runtime.agent.service.logging;

import com.solace.maas.ep.runtime.agent.processor.LoggingProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoggingService {
    private final Map<String, LoggingProcessor> logStore = new HashMap<>();

    public void addLoggingProcessor(String scanId, LoggingProcessor loggingProcessor) {
        logStore.put(scanId, loggingProcessor);
    }

    public void removeLoggingProcessor(String scanId) {
        logStore.remove(scanId);
    }

    public boolean hasLoggingProcessor(String scanId) {
        return logStore.containsKey(scanId);
    }

    public LoggingProcessor getLoggingProcessor(String scanId) {
        return logStore.get(scanId);
    }
}

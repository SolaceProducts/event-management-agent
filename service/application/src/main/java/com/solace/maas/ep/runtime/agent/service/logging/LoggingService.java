package com.solace.maas.ep.runtime.agent.service.logging;

import com.solace.maas.ep.runtime.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.runtime.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.runtime.agent.processor.LoggingProcessor;
import com.solace.maas.ep.runtime.agent.repository.model.route.RouteEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LoggingService {
    private final Map<String, List<LoggingProcessor>> logStore = new HashMap<>();

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

    public RouteEntity creatLoggingRoute(String messagingServiceId) {
        MessagingServiceRouteDelegate scanDelegate =
                PluginLoader.findPlugin("SCAN_LOGS");

        List<RouteBundle> routes = scanDelegate.generateRouteList(
                List.of(),
                List.of(),
                "",
                messagingServiceId);

        return RouteEntity.builder()
                .id(routes.stream().findFirst().orElseThrow().getRouteId())
                .active(true)
                .build();
    }
}

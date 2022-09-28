package com.solace.maas.ep.runtime.agent.service.logging;

import com.solace.maas.ep.runtime.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.runtime.agent.logging.FileLoggerFactory;
import com.solace.maas.ep.runtime.agent.logging.StreamLoggerFactory;
import com.solace.maas.ep.runtime.agent.logging.StreamingAppender;
import com.solace.maas.ep.runtime.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.runtime.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.runtime.agent.processor.LoggingProcessor;
import com.solace.maas.ep.runtime.agent.repository.model.route.RouteEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;
import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.SCAN_ID;
import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.SCHEDULE_ID;

@Slf4j
@Service
public class LoggingService {
    private final Map<String, List<LoggingProcessor>> logStore = new HashMap<>();

    private final FileLoggerFactory fileLoggerFactory;

    private final StreamLoggerFactory streamLoggerFactory;

    private final EventPortalProperties eventPortalProperties;

    public LoggingService(FileLoggerFactory fileLoggerFactory, StreamLoggerFactory streamLoggerFactory,
                          EventPortalProperties eventPortalProperties) {
        this.fileLoggerFactory = fileLoggerFactory;
        this.streamLoggerFactory = streamLoggerFactory;
        this.eventPortalProperties = eventPortalProperties;
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

    public void createScanFileLogger(Map<String, String> details) {
        String scanId = details.get(SCAN_ID);

        fileLoggerFactory.create();
        LoggingProcessor loggingProcessor = new LoggingProcessor("FileAppender");
        addLoggingProcessor(scanId, loggingProcessor);
    }

    public void createScanStreamingLogger(Map<String, String> details) {
        String groupId = details.get(SCHEDULE_ID);
        String scanId = details.get(SCAN_ID);
        String messagingServiceId = details.get(MESSAGING_SERVICE_ID);

        StreamingAppender streamingAppender = streamLoggerFactory.getStreamingAppender();
        streamingAppender.setGroupId(groupId);
        streamingAppender.setScanId(scanId);
        streamingAppender.setMessagingServiceId(messagingServiceId);
        streamingAppender.setStandalone(eventPortalProperties.getGateway().getMessaging().isStandalone());
        streamingAppender.setRoute(creatLoggingRoute(messagingServiceId));

        LoggingProcessor streamingAppenderProcessor = new LoggingProcessor("StreamingAppender");
        addLoggingProcessor(scanId, streamingAppenderProcessor);
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

    public void prepareLoggers(String groupId, String scanId, String messagingServiceId) {
        if (!hasLoggingProcessor(scanId)) {
            Map<String, String> details = new HashMap<>();
            MDC.put(SCAN_ID, scanId);
            details.put(SCAN_ID, scanId);
            details.put(MESSAGING_SERVICE_ID, messagingServiceId);
            details.put(SCHEDULE_ID, groupId);

            createScanFileLogger(details);
            createScanStreamingLogger(details);
        }
    }
}

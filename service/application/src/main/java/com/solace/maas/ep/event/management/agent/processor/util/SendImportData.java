package com.solace.maas.ep.event.management.agent.processor.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class SendImportData {
    private final ProducerTemplate producerTemplate;

    public SendImportData(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public CompletableFuture<Exchange> sendImportDataAsync(String groupId, String scanId, String scanType, String messagingServiceId, String body) {
        return producerTemplate.asyncSend("seda:importDataPublisher", exchange -> {
            // Need to set headers to let the Route have access to the Scan ID, Group ID, and Scan Type, and Messaging Service ID.
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);

            exchange.getIn().setBody(body);
        }).whenComplete((exchange, exception) -> {
            if (exception != null) {
                log.error("Exception occurred while executing route {} for scan request: {}.", "seda:importDataPublisher",
                        scanId, exception);
            } else {
                log.trace("Successfully completed route {} for scan request: {}", "seda:importDataPublisher", scanId);
            }
        });
    }

    public CompletableFuture<Exchange> sendImportLogsAsync(String scanId, String scanType, String messagingServiceId, ILoggingEvent event) {
        RouteEntity route = creatLoggingRoute(scanType, messagingServiceId);

        return producerTemplate.asyncSend(route.getId(), exchange -> {
            // Need to set headers to let the Route have access to the Scan ID, Scan Type, and Messaging Service ID.
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);

            exchange.getIn().setBody(event);

        }).whenComplete((exchange, exception) -> {
            if (exception != null) {
                log.error("Exception occurred while executing route publishLogs for scan {}.", scanId, exception);
            }
        });
    }

    protected RouteEntity creatLoggingRoute(String scanType, String messagingServiceId) {
        MessagingServiceRouteDelegate scanDelegate =
                PluginLoader.findPlugin("SCAN_LOGS");

        List<RouteBundle> routes = scanDelegate.generateRouteList(
                List.of(),
                List.of(),
                scanType,
                messagingServiceId);

        return RouteEntity.builder()
                .id(routes.stream().findFirst().orElseThrow().getRouteId())
                .active(true)
                .build();
    }
}

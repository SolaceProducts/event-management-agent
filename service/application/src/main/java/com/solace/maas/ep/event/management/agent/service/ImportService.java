package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class ImportService {

    private final ProducerTemplate producerTemplate;

    public ImportService(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public void importData(ImportRequestBO importRequestBO) throws IOException {
        InputStream file = importRequestBO.getDataFile().getInputStream();
        String messagingServiceId = importRequestBO.getMessagingServiceId();
        String scheduleId = importRequestBO.getScheduleId();
        String scanId = importRequestBO.getScanId();

        initiateImport(file, messagingServiceId, scheduleId, scanId);
    }

    protected void initiateImport(InputStream file, String messagingServiceId,
                                  String scheduleId, String scanId) {
        RouteEntity route = createRoute(messagingServiceId);

        producerTemplate.asyncSend(route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, scheduleId);
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);

            exchange.getIn().setBody(file);
        }).whenComplete((exchange, exception) -> {
            if (exception != null) {
                log.error("Exception occurred while executing route {} for scan request: {}.", route.getId(), scanId, exception);
            } else {
                log.debug("Successfully completed route {} for scan request: {}", route.getId(), scanId);
            }
        });
    }

    protected RouteEntity createRoute(String messagingServiceId) {
        MessagingServiceRouteDelegate delegate =
                PluginLoader.findPlugin("MANUAL_IMPORT");

        List<RouteBundle> routes = delegate.generateRouteList(
                List.of(),
                List.of(),
                "MANUAL_SCAN_DATA_IMPORT",
                messagingServiceId);

        return RouteEntity.builder()
                .id(routes.stream().findFirst().orElseThrow().getRouteId())
                .active(true)
                .build();
    }
}

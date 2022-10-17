package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.model.ScanStatus;
import com.solace.maas.ep.common.model.ScanStatusType;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.camel.util.function.Suppliers.constant;

@Slf4j
@Component
public class RouteCompleteProcessorImpl extends RouteCompleteProcessor {
    private final ProducerTemplate producerTemplate;
    private final ScanStatusRepository scanStatusRepository;

    public RouteCompleteProcessorImpl(ProducerTemplate producerTemplate, ScanStatusRepository scanStatusRepository) {
        this.producerTemplate = producerTemplate;
        this.scanStatusRepository = scanStatusRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        if (cause != null) {
            log.error("Error has occurred: ", cause);

            // Sending Error message to client
            exchange.getIn().setHeader("SCAN_ERROR", constant(true));
        } else {
            String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
            String scanType = (String) exchange.getIn().getHeader(RouteConstants.SCAN_TYPE);
            String groupId = (String) exchange.getIn().getHeader(RouteConstants.SCHEDULE_ID);
            String messagingServiceId = (String) exchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID);

            ScanStatusEntity scanStatusEntity = ScanStatusEntity.builder()
                    .scanId(scanId)
                    .scanType(scanType)
                    .status(ScanStatus.COMPLETE.name())
                    .build();

            save(scanStatusEntity);

            sendScanDataStatus(scanId, scanType, groupId, messagingServiceId, ScanStatus.COMPLETE, ScanStatusType.PER_ROUTE);
            log.info("Route {} completed for scan request {}", scanType, scanId);
        }
    }

    private RouteEntity createScanStatusRoute(String scanType, String messagingServiceId) {
        MessagingServiceRouteDelegate delegate = PluginLoader.findPlugin("SCAN_STATUS");

        List<RouteBundle> routes = delegate.generateRouteList(
                List.of(),
                List.of(),
                scanType,
                messagingServiceId);

        return RouteEntity.builder()
                .id(routes.stream().findFirst().orElseThrow().getRouteId())
                .active(true)
                .build();
    }

    public void sendScanDataStatus(String scanId, String scanType, String groupId,
                                   String messagingServiceId, ScanStatus status, ScanStatusType statusType) {
        RouteEntity route = createScanStatusRoute(scanType, messagingServiceId);

        producerTemplate.send(route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, status);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, statusType);
        });
    }

    public void sendScanStatus(String scanId, List<String> scanTypes, String groupId,
                               String messagingServiceId, ScanStatus status, ScanStatusType statusType) {
        RouteEntity route = createScanStatusRoute(scanTypes.stream().findFirst().orElseThrow(),
                messagingServiceId);

        producerTemplate.send(route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPES, scanTypes);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, status);
            exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, statusType);
        });
    }

    protected ScanStatusEntity save(ScanStatusEntity scanStatusEntity) {
        return scanStatusRepository.save(scanStatusEntity);
    }
}

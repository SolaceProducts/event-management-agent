package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import com.solace.maas.ep.event.management.agent.service.ScanStatusService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_SCAN_EVENT_CYCLE_TIME;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORG_ID_TAG;

@Slf4j
@Component
public class RouteCompleteProcessorImpl extends RouteCompleteProcessor {
    private final ScanStatusService scanStatusService;
    private final ScanService scanService;
    private final MeterRegistry meterRegistry;
    private final EventPortalProperties eventPortalProperties;

    public RouteCompleteProcessorImpl(ScanStatusService scanStatusService,
                                      ScanService scanService,
                                      MeterRegistry meterRegistry,
                                      EventPortalProperties eventPortalProperties) {
        super();
        this.scanStatusService = scanStatusService;
        this.scanService = scanService;
        this.meterRegistry = meterRegistry;
        this.eventPortalProperties = eventPortalProperties;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);
        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        String orgId = (String) exchange.getIn().getHeader(RouteConstants.ORG_ID);
        String scanType = getScanType(exchange);

        // ToDo: remove this before merging the PR
        log.info("will2 save scan status as complete for scanId {} orgId {} scanType {}", scanId, orgId, scanType);

        scanStatusService.save(scanType, scanId, ScanStatus.COMPLETE);
        if (Boolean.TRUE.equals(eventPortalProperties.getManaged()) && scanService.isScanComplete(scanId)) {
            registerScanCycleTime(scanId, orgId);
        }
    }

    private void registerScanCycleTime(String scanId, String orgId) {
        ScanEntity scanEntity = scanService.getById(scanId);
        long now = System.currentTimeMillis();
        Timer jobCycleTime = Timer
                .builder(MAAS_EMA_SCAN_EVENT_CYCLE_TIME)
                .tag(ORG_ID_TAG, orgId)
                .register(meterRegistry);

        // ToDo: remove this before merging the PR
        log.info("moodi2 xxx scanEntity {} now {} scanEntity.getCreatedAt().toEpochMilli() {} now-createdAt {}",
                scanEntity, now, scanEntity.getCreatedAt().toEpochMilli(), now - scanEntity.getCreatedAt().toEpochMilli());

        jobCycleTime.record(now - scanEntity.getCreatedAt().toEpochMilli(), TimeUnit.MILLISECONDS);
    }
}

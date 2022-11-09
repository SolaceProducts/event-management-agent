package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatusType;
import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RouteCompleteProcessorImpl extends RouteCompleteProcessor {
    private final ScanStatusRepository scanStatusRepository;

    public RouteCompleteProcessorImpl(ScanStatusRepository scanStatusRepository) {
        this.scanStatusRepository = scanStatusRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.COMPLETE);
        exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.PER_ROUTE);

        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        String scanType = (String) exchange.getIn().getHeader(RouteConstants.SCAN_TYPE);

        ScanStatusEntity scanStatusEntity = ScanStatusEntity.builder()
                .scanId(scanId)
                .scanType(scanType)
                .status(ScanStatus.COMPLETE.name())
                .build();

        save(scanStatusEntity);

        log.trace("Scan request [{}]: The status of [{}] is: [{}].", scanId, scanType, ScanStatus.COMPLETE.name());
    }

    protected ScanStatusEntity save(ScanStatusEntity scanStatusEntity) {
        return scanStatusRepository.save(scanStatusEntity);
    }
}

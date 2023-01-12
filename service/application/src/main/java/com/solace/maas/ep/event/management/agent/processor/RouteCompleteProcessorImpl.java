package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanStatusEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.List;

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

        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);

        String scanType;

        if (exchange.getIn().getHeader(RouteConstants.SCAN_TYPE) instanceof List<?>) {
            scanType = (String) exchange.getIn().getBody();
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
        } else {
            scanType = (String) exchange.getIn().getHeader(RouteConstants.SCAN_TYPE);
        }

        ScanStatusEntity scanStatusEntity = ScanStatusEntity.builder()
                .scanId(scanId)
                .scanType(scanType)
                .status(ScanStatus.COMPLETE.name())
                .build();

        save(scanStatusEntity);
    }

    protected ScanStatusEntity save(ScanStatusEntity scanStatusEntity) {
        return scanStatusRepository.save(scanStatusEntity);
    }
}

package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportFileProcessor implements Processor {
    private final ManualImportRepository manualImportRepository;

    public ScanDataImportFileProcessor(ManualImportRepository manualImportRepository) {
        this.manualImportRepository = manualImportRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> properties = exchange.getIn().getHeaders();

        String fileName = (String) properties.get("CamelFileName");
        String groupId = (String) properties.get(RouteConstants.SCHEDULE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);

        log.trace("reading file: {}", fileName);

        ManualImportEntity manualImportEntity = ManualImportEntity.builder()
                .fileName(fileName)
                .groupId(groupId)
                .scanId(scanId)
                .scanType(scanType)
                .build();

        save(manualImportEntity);
    }

    private void save(ManualImportEntity manualImportEntity) {
        manualImportRepository.save(manualImportEntity);
    }
}

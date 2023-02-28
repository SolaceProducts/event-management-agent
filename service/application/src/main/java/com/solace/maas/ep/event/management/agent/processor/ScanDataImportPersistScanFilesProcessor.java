package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import com.solace.maas.ep.event.management.agent.service.ManualImportService;
import com.solace.maas.ep.event.management.agent.service.ScanTypeService;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportPersistScanFilesProcessor implements Processor {

    private final ScanTypeService scanTypeService;
    private final ManualImportService manualImportService;
    private final IDGenerator idGenerator;

    public ScanDataImportPersistScanFilesProcessor(ScanTypeService scanTypeService, ManualImportService manualImportService,
                                                   IDGenerator idGenerator) {
        this.scanTypeService = scanTypeService;
        this.manualImportService = manualImportService;
        this.idGenerator = idGenerator;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> properties = exchange.getIn().getHeaders();

        String fileName = (String) properties.get("CamelFileName");
        String groupId = (String) properties.get(RouteConstants.SCHEDULE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);

        log.trace("reading file: {}", fileName);

        ScanTypeEntity scanTypeEntity = scanTypeService.findByNameAndScanId(scanType, scanId)
                .orElseThrow(() -> new RuntimeException("Can't apply Scan Status to Scan that doesn't exist!"));

        ManualImportEntity manualImportEntity = ManualImportEntity.builder()
                .id(idGenerator.generateRandomUniqueId())
                .fileName(fileName)
                .groupId(groupId)
                .scanType(scanTypeEntity)
                .build();

        manualImportService.save(manualImportEntity);
    }
}

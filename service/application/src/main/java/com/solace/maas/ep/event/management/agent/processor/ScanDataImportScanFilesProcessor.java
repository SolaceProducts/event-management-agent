package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.util.SendImportData;
import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportScanFilesProcessor implements Processor {
    private final SendImportData sendImportData;
    private final ManualImportRepository manualImportRepository;

    public ScanDataImportScanFilesProcessor(SendImportData sendImportData,
                                            ManualImportRepository manualImportRepository) {
        this.sendImportData = sendImportData;
        this.manualImportRepository = manualImportRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileName = (String) exchange.getIn().getHeader("CamelFileName");

        log.trace("reading file: {}", fileName);

        String groupId = (String) exchange.getIn().getHeader(RouteConstants.SCHEDULE_ID);
        String scanId = (String) exchange.getIn().getHeader(RouteConstants.SCAN_ID);
        String messagingServiceId = (String) exchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID);
        String body = (String) exchange.getIn().getBody();

        String scanType = StringUtils.substringAfterLast(fileName, "/").replace(".json", "");

        ManualImportEntity manualImportEntity = ManualImportEntity.builder()
                .fileName(fileName)
                .groupId(groupId)
                .scanId(scanId)
                .scanType(scanType)
                .build();

        save(manualImportEntity);

        log.info("Importing {} for schedule Id: {} scan request: {}", scanType, groupId, scanId);
        sendImportData.sendImportDataAsync(groupId, scanId, scanType, messagingServiceId, body);
    }

    private void save(ManualImportEntity manualImportEntity) {
        manualImportRepository.save(manualImportEntity);
    }
}

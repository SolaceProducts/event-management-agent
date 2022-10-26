package com.solace.maas.ep.event.management.agent.processor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.util.SendImportData;
import com.solace.maas.ep.event.management.agent.repository.manualimport.ManualImportRepository;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportEntity;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportAllFilesProcessor implements Processor {
    private final SendImportData sendImportData;
    private final ManualImportRepository manualImportRepository;

    public ScanDataImportAllFilesProcessor(SendImportData sendImportData,
                                           ManualImportRepository manualImportRepository) {
        this.sendImportData = sendImportData;
        this.manualImportRepository = manualImportRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String messagingServiceId = (String) exchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID);
        String fileName = (String) exchange.getIn().getHeader("CamelFileName");
        log.trace("reading file: {}", fileName);

        if (fileName.contains(".json")) {
            String body = (String) exchange.getIn().getBody();

            String[] scanDetails = StringUtils.split(fileName, '/');
            String groupId = scanDetails[1];
            String scanId = scanDetails[2];
            String scanType = scanDetails[3].replace(".json", "");

            ManualImportEntity manualImportEntity = ManualImportEntity.builder()
                    .fileName(fileName)
                    .groupId(groupId)
                    .scanId(scanId)
                    .scanType(scanType)
                    .build();

            save(manualImportEntity);

            log.info("Importing data file {}", fileName);

            sendImportData.sendImportDataAsync(groupId, scanId, scanType, messagingServiceId, body);
        } else if (fileName.contains(".log") && !fileName.contains("general-logs")) {
            String body = (String) exchange.getIn().getBody();
            String scanId = StringUtils.substringAfterLast(fileName, "/").replace(".log", "");

            ILoggingEvent event = prepareLoggingEvent(body);

            ManualImportEntity manualImportEntity = ManualImportEntity.builder()
                    .fileName(fileName)
                    .groupId(null)
                    .scanId(scanId)
                    .scanType("ImportedLogs")
                    .build();

            save(manualImportEntity);

            log.info("Importing log file {}", fileName);
            sendImportData.sendImportLogsAsync(scanId, "ImportedLogs", messagingServiceId, event);
        }
    }

    private ILoggingEvent prepareLoggingEvent(String body) {
        ch.qos.logback.classic.Logger logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        return new LoggingEvent(null, logger, Level.ALL, body,
                new Throwable("throwable message"), null);
    }

    private void save(ManualImportEntity manualImportEntity) {
        manualImportRepository.save(manualImportEntity);
    }
}
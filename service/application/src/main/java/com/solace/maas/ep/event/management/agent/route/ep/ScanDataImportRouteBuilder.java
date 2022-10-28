package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatusType;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportAllFilesProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportGroupFilesProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportScanFilesProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.camel.support.builder.PredicateBuilder.and;
import static org.apache.camel.support.builder.PredicateBuilder.not;
import static org.apache.camel.support.builder.PredicateBuilder.or;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportRouteBuilder extends RouteBuilder {
    private static final List<String> fileList = new ArrayList<>();
    private final ScanDataImportScanFilesProcessor scanDataImportScanFilesProcessor;
    private final ScanDataImportGroupFilesProcessor scanDataImportGroupFilesProcessor;
    private final ScanDataImportAllFilesProcessor scanDataImportAllFilesProcessor;
    private final ScanDataImportProcessor scanDataImportProcessor;

    @Autowired
    public ScanDataImportRouteBuilder(ScanDataImportScanFilesProcessor scanDataImportScanFilesProcessor,
                                      ScanDataImportGroupFilesProcessor scanDataImportGroupFilesProcessor,
                                      ScanDataImportAllFilesProcessor scanDataImportAllFilesProcessor,
                                      ScanDataImportProcessor scanDataImportProcessor) {
        super();
        this.scanDataImportScanFilesProcessor = scanDataImportScanFilesProcessor;
        this.scanDataImportGroupFilesProcessor = scanDataImportGroupFilesProcessor;
        this.scanDataImportAllFilesProcessor = scanDataImportAllFilesProcessor;
        this.scanDataImportProcessor = scanDataImportProcessor;
    }

    @Override
    public void configure() {
        from("seda:manualImport?blockWhenFull=true&size=100")
                .routeId("manualImport")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .split(new ZipSplitter())
                .streaming()
                .aggregationStrategy(new FileListAggregationStrategy())
                .filter(or(header("CamelFileName").endsWith(".json"),
                        and(header("CamelFileName").endsWith(".log"),
                                not(header("CamelFileName").contains("general-log")))))

                .convertBodyTo(String.class)
                .to("file://unzip_data_collection")
                .end()
                .to("seda:readFiles");

        from("seda:readFiles")
                .pollEnrich()
                .simple("file://unzip_data_collection?noop=true&recursive=true&idempotent=false")
                .aggregationStrategy(new FileParseAggregationStrategy())
                .setHeader("FILE_NAMES", constant(fileList))
                .to("seda:processFiles");

        from("seda:processFiles")
                .convertBodyTo(String.class)
                .split().tokenize("\\n").streaming()
                .choice()
                // Covers the scenario where schedule id scan id and scan are specified.
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header("CamelFileName").contains(header(RouteConstants.SCAN_ID))))
                .setHeader(RouteConstants.SCAN_STATUS, constant(ScanStatus.IN_PROGRESS))
                .setHeader(RouteConstants.SCAN_STATUS_TYPE, constant(ScanStatusType.OVERALL))
                .process(scanDataImportScanFilesProcessor)
                .endChoice()

                // Covers the scenario where only schedule is specified. All scanId folders in the schedule directory will be processed.
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header(RouteConstants.SCAN_ID).isNull()))
                .setHeader(RouteConstants.SCAN_STATUS, constant(ScanStatus.IN_PROGRESS))
                .setHeader(RouteConstants.SCAN_STATUS_TYPE, constant(ScanStatusType.OVERALL))
                .process(scanDataImportGroupFilesProcessor)
                .endChoice()

                // Nothing is specified. Imports all scan data and log data
                .when(and(header(RouteConstants.SCHEDULE_ID).isNull(), header(RouteConstants.SCAN_ID).isNull()))
                .setHeader(RouteConstants.SCAN_STATUS, constant(ScanStatus.IN_PROGRESS))
                .setHeader(RouteConstants.SCAN_STATUS_TYPE, constant(ScanStatusType.OVERALL))
                .process(scanDataImportAllFilesProcessor)
                .endChoice()

                .end();

        from("seda:importDataPublisher?blockWhenFull=true&size=100")
                .routeId("importDataPublisher")
                .process(scanDataImportProcessor)
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end();
    }

    public static class FileListAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            String filename = (String) newExchange.getIn().getHeader("CamelFileName");
            if (filename.endsWith(".json") || (filename.endsWith(".log") && !filename.contains("general-logs"))) {
                fileList.add(filename);
            }
            return newExchange;
        }
    }

    public static class FileParseAggregationStrategy implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            newExchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID,
                    oldExchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID));
            newExchange.getIn().setHeader(RouteConstants.SCAN_ID,
                    oldExchange.getIn().getHeader(RouteConstants.SCAN_ID));
            newExchange.getIn().setHeader(RouteConstants.SCHEDULE_ID,
                    oldExchange.getIn().getHeader(RouteConstants.SCHEDULE_ID));
            newExchange.getIn().setHeader(RouteConstants.SCAN_TYPE,
                    oldExchange.getIn().getHeader(RouteConstants.SCAN_TYPE));

            return newExchange;
        }
    }
}

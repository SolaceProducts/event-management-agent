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
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.camel.support.builder.PredicateBuilder.and;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportRouteBuilder extends RouteBuilder {
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
                .setHeader(RouteConstants.AGGREGATION_ID, constant("id"))
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .split(new ZipSplitter())
                .streaming()
                .convertBodyTo(String.class)

                .choice()
                // Covers the scenario where schedule id scan id and scan are specified.
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header("CamelFileName").contains(header(RouteConstants.SCAN_ID))))
                .aggregate(header("CamelFileName"), new ArrayListAggregationStrategy())
                .completionTimeout(500)
                .setHeader(RouteConstants.SCAN_STATUS, constant(ScanStatus.IN_PROGRESS))
                .setHeader(RouteConstants.SCAN_STATUS_TYPE, constant(ScanStatusType.PER_ROUTE))
                .process(scanDataImportScanFilesProcessor)
                .endChoice()

                // Covers the scenario where only schedule is specified. All scanId folders in the schedule directory will be processed.
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header(RouteConstants.SCAN_ID).isNull()))
                .aggregate(header("CamelFileName"), new ArrayListAggregationStrategy())
                .completionTimeout(500)
                .setHeader(RouteConstants.SCAN_STATUS, constant(ScanStatus.IN_PROGRESS))
                .setHeader(RouteConstants.SCAN_STATUS_TYPE, constant(ScanStatusType.PER_ROUTE))
                .process(scanDataImportGroupFilesProcessor)
                .endChoice()

                // Nothing is specified. Imports all scan data and log data
                .when(and(header(RouteConstants.SCHEDULE_ID).isNull(), header(RouteConstants.SCAN_ID).isNull()))
                .aggregate(header("CamelFileName"), new ArrayListAggregationStrategy())
                .completionTimeout(500)
                .setHeader(RouteConstants.SCAN_STATUS, constant(ScanStatus.IN_PROGRESS))
                .setHeader(RouteConstants.SCAN_STATUS_TYPE, constant(ScanStatusType.PER_ROUTE))
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

    public static class ArrayListAggregationStrategy implements AggregationStrategy {
        List<String> fileList = new ArrayList<>();

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            String filename = (String) newExchange.getIn().getHeader("CamelFileName");
            fileList.add(filename);
            newExchange.getIn().setHeader("FILE_NAMES", fileList);
            return newExchange;
        }
    }
}

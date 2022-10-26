package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportAllFilesProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportGroupFilesProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportScanFilesProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

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
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .split(new ZipSplitter())
                .streaming()
                .choice()
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header("CamelFileName").contains(header(RouteConstants.SCAN_ID))))
                .convertBodyTo(String.class)
                .process(scanDataImportScanFilesProcessor)
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header(RouteConstants.SCAN_ID).isNull()))
                .convertBodyTo(String.class)
                .process(scanDataImportGroupFilesProcessor)
                .when(and(header(RouteConstants.SCHEDULE_ID).isNull(), header(RouteConstants.SCAN_ID).isNull()))
                .convertBodyTo(String.class)
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
}

package com.solace.maas.ep.event.management.agent.route.manualImport;

import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPersistScanFilesProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportStatusProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.aggregation.FileParseAggregationStrategy;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportStreamFilesRouteBuilder extends RouteBuilder {

    private final ScanDataImportStatusProcessor scanDataImportStatusProcessor;

    private final ScanDataImportPersistScanFilesProcessor scanDataImportPersistScanFilesProcessor;

    public ScanDataImportStreamFilesRouteBuilder(ScanDataImportStatusProcessor scanDataImportStatusProcessor,
                                                 ScanDataImportPersistScanFilesProcessor scanDataImportPersistScanFilesProcessor) {
        this.scanDataImportStatusProcessor = scanDataImportStatusProcessor;
        this.scanDataImportPersistScanFilesProcessor = scanDataImportPersistScanFilesProcessor;
    }

    @Override
    public void configure() throws Exception {

        from("direct:parseAndStreamImportFiles")
                .routeId("parseAndStreamImportFiles")
                .split().body()
                .streaming()
                .process(scanDataImportStatusProcessor)
                .to("direct:scanStatusPublisher")

                .pollEnrich()
                .simple("file://data_collection/import/unzipped_data_collection/${header.IMPORT_ID}?" +
                        "fileName=${body.fileName}&noop=true&idempotent=false")
                .aggregationStrategy(new FileParseAggregationStrategy())
                .process(scanDataImportPersistScanFilesProcessor)
                .split().tokenize("\\n").streaming()
                .to("direct:streamImportFiles")
                .end();


        from("direct:streamImportFiles")
                .routeId("streamImportFiles")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .to("direct:importToEP")
                .choice().when(header(Exchange.SPLIT_COMPLETE).isEqualTo(true))
                .to("direct:processEndOfFileImportStatus");


        from("direct:processEndOfFileImportStatus")
                .routeId("processEndOfFileImportStatus")
                .setHeader("FILE_IMPORTING_COMPLETE", constant(true))
                .to("direct:processScanStatus");
    }
}

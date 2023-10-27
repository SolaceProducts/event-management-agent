package com.solace.maas.ep.event.management.agent.route.manualImport;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptionhandlers.ScanDataImportExceptionHandler;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportOverAllStatusProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportParseMetaInfFileProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPersistFilePathsProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPersistScanDataProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPublishImportScanEventProcessor;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.IMPORT_ID;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanDataImportParseMetaInfFileRouteBuilder extends RouteBuilder {

    private final ScanDataImportParseMetaInfFileProcessor scanDataImportParseMetaInfFileProcessor;
    private final ScanDataImportPersistFilePathsProcessor scanDataImportPersistFilePathsProcessor;
    private final ScanDataImportPublishImportScanEventProcessor scanDataImportPublishImportScanEventProcessor;
    private final ScanDataImportOverAllStatusProcessor scanDataImportOverAllStatusProcessor;
    private final ScanDataImportPersistScanDataProcessor scanDataImportPersistScanDataProcessor;

    public ScanDataImportParseMetaInfFileRouteBuilder(ScanDataImportParseMetaInfFileProcessor scanDataImportParseMetaInfFileProcessor,
                                                      ScanDataImportPersistFilePathsProcessor scanDataImportPersistFilePathsProcessor,
                                                      ScanDataImportPublishImportScanEventProcessor scanDataImportPublishImportScanEventProcessor,
                                                      ScanDataImportOverAllStatusProcessor scanDataImportOverAllStatusProcessor,
                                                      ScanDataImportPersistScanDataProcessor scanDataImportPersistScanDataProcessor
    ) {
        super();
        this.scanDataImportParseMetaInfFileProcessor = scanDataImportParseMetaInfFileProcessor;
        this.scanDataImportPersistFilePathsProcessor = scanDataImportPersistFilePathsProcessor;
        this.scanDataImportPublishImportScanEventProcessor = scanDataImportPublishImportScanEventProcessor;
        this.scanDataImportOverAllStatusProcessor = scanDataImportOverAllStatusProcessor;
        this.scanDataImportPersistScanDataProcessor = scanDataImportPersistScanDataProcessor;
    }

    @Override
    public void configure() {

        from("direct:parseMetaInfoAndPerformHandShakeWithEP")
                .routeId("parseMetaInfoAndPerformHandShakeWithEP")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .pollEnrich()
                .simple("file://data_collection/import/unzipped_data_collection/${header." + IMPORT_ID + "}" +
                        "?fileName=META_INF.json&noop=true&idempotent=false")
                .aggregationStrategy(new UseLatestAggregationStrategy())
                .convertBodyTo(String.class)
                .unmarshal().json(JsonLibrary.Jackson, MetaInfFileBO.class)
                .process(scanDataImportParseMetaInfFileProcessor)
                .process(scanDataImportPersistFilePathsProcessor)
                .process(scanDataImportPublishImportScanEventProcessor);

        from("direct:sendOverAllInProgressImportStatus")
                .routeId("sendOverAllInProgressImportStatus")
                .process(scanDataImportOverAllStatusProcessor)
                .log("Scan import request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: Starting scan data import process.")
                .process(scanDataImportPersistScanDataProcessor)
                .to("direct:overallScanStatusPublisher?block=false&failIfNoConsumers=false");
    }
}

package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportMetaInfFileProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.aggregation.MetaInfFileParseAggregationStrategy;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportMetaInfFileRouteBuilder extends RouteBuilder {

    private final ScanDataImportMetaInfFileProcessor scanDataImportMetaInfFileProcessor;

    public ScanDataImportMetaInfFileRouteBuilder(ScanDataImportMetaInfFileProcessor scanDataImportMetaInfFileProcessor) {
        this.scanDataImportMetaInfFileProcessor = scanDataImportMetaInfFileProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("seda:processOverAllImportStatus")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .split(new ZipSplitter())
                .streaming()
                .process(scanDataImportMetaInfFileProcessor)
                .filter(header("fileName").isEqualTo("META_INF.json"))
                .convertBodyTo(String.class)
                .to("file://data_collection/unzip_data_collection")
                .to("seda:readMetaInfFile");


        from("seda:readMetaInfFile")
                .process(scanDataImportMetaInfFileProcessor)
                .pollEnrich()
                .simple("file://data_collection/unzip_data_collection/" +
                        "${header.filepath}/${header." + RouteConstants.SCHEDULE_ID + "}/${header." + RouteConstants.SCAN_ID +
                        "}?fileName=${header.fileName}&noop=true&idempotent=false")
                .aggregationStrategy(new MetaInfFileParseAggregationStrategy())
                .to("seda:parseMetaInfAndSendStatus");


        from("seda:parseMetaInfAndSendStatus")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(RouteConstants.SCAN_TYPE, simple("${body.get('files')}"))
                .to("seda:scanStatusPublisher");
    }
}

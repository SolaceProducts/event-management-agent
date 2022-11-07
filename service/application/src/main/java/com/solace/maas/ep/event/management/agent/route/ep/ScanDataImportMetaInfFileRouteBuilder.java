package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportMetaInfFileProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.aggregation.FileParseAggregationStrategy;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import static org.apache.camel.support.builder.PredicateBuilder.and;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportMetaInfFileRouteBuilder extends RouteBuilder {

    private static final String POLL_ENRICH_FILE_PATH = "file://data_collection/unzip_data_collection/" +
            "${header.filepath}/${header." + RouteConstants.SCHEDULE_ID + "}/${header." + RouteConstants.SCAN_ID +
            "}?fileName=${header.fileName}&noop=true&idempotent=false";

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
                .process(exchange -> {
                    String camelFileName = (String) exchange.getIn().getHeader("CamelFileName");
                    String fileName = StringUtils.substringAfterLast(camelFileName, "/");
                    exchange.getIn().setHeader("fileName", fileName);
                })
                .process(exchange -> {
                    String camelFileName = (String) exchange.getIn().getHeader("CamelFileName");
                    String fileName = StringUtils.substringAfterLast(camelFileName, "/");
                    exchange.getIn().setHeader("fileName", fileName);
                })
                .filter(header("fileName").isEqualTo("META_INF.json"))
                .convertBodyTo(String.class)
                .to("file://data_collection/unzip_data_collection")
                .to("seda:readFiles");


        from("seda:readFiles")
                .choice()
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header("CamelFileName").contains(header(RouteConstants.SCAN_ID))))
                .to("seda:pollMetaInfFile")
                .endChoice()

                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header(RouteConstants.SCAN_ID).isNull()))
                .to("seda:pollMetaInfFile")
                .endChoice()

                .when(and(header(RouteConstants.SCHEDULE_ID).isNull(), header(RouteConstants.SCAN_ID).isNull()))
                .to("seda:pollMetaInfFile")
                .endChoice()

                .end();


        from("seda:pollMetaInfFile")
                .process(scanDataImportMetaInfFileProcessor)
                .pollEnrich()
                .simple(POLL_ENRICH_FILE_PATH)
                .aggregationStrategy(new FileParseAggregationStrategy())
                .to("seda:parseMetaInfAndSendStatus");


        from("seda:parseMetaInfAndSendStatus")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(RouteConstants.SCAN_TYPE, simple("${body.get('files')}"))
                .to("seda:scanStatusPublisher");
    }
}

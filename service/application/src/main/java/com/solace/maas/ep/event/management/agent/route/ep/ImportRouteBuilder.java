package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatusType;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.dataformat.ZipFileDataFormat;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.aggregate.UseOriginalAggregationStrategy;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
//@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ImportRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        ZipFileDataFormat zf = new ZipFileDataFormat();
        zf.setUsingIterator("true");

        from("seda:importScanData")
                .setProperty("IMPORT_ID", header("IMPORT_ID"))
                .setHeader(Exchange.FILE_NAME, simple("${header.IMPORT_ID}.zip"))
                .toD("file://data_collection/import/compressed")
                .to("direct:checkZipFileSize");

        from("direct:checkZipFileSize")
                .pollEnrich()
                .simple("file://data_collection/import/compressed?fileName=${header." + Exchange.FILE_NAME + "}&noop=true&idempotent=false")
                .unmarshal(zf)
                .split(bodyAs(Iterator.class))
                .aggregate(exchangeProperty("IMPORT_ID"), new UseLatestAggregationStrategy())
                .completionPredicate(header(Exchange.SPLIT_COMPLETE).isEqualTo(true))
                .setProperty("FILE_LIST_SIZE", header(Exchange.SPLIT_SIZE))
                .log("FILE SIZE = ${exchangeProperty.FILE_LIST_SIZE}")
                .to("direct:unzipImportFile");

        from("direct:unzipImportFile")
                .pollEnrich()
                .simple("file://${header." + Exchange.FILE_PARENT + "}?fileName=${header." + Exchange.FILE_NAME_ONLY + "}&noop=true&idempotent=false")
                .split(new ZipSplitter())
                .streaming()
                .toD("file://data_collection/unzip_data_collection/${header.IMPORT_ID}")
                .aggregate(exchangeProperty("IMPORT_ID"), new UseLatestAggregationStrategy())
                .completionSize(exchangeProperty("FILE_LIST_SIZE"))
                .to("direct:parseMetaInfo");


        from("direct:parseMetaInfo")
                .pollEnrich()
                .simple("file://data_collection/unzip_data_collection/${header.IMPORT_ID}?fileName=META_INF.json&noop=true&idempotent=false")
                .aggregationStrategy(new UseLatestAggregationStrategy())
                .convertBodyTo(String.class)
                .unmarshal().json(JsonLibrary.Jackson, MetaInfFileBO.class)
                .process(exchange -> {
                    MetaInfFileBO metaInfFileBO = exchange.getIn().getBody(MetaInfFileBO.class);

                    exchange.getIn().setHeader(RouteConstants.SCAN_ID, metaInfFileBO.getScanId());
                    exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, metaInfFileBO.getMessagingServiceId());
                    exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, metaInfFileBO.getScheduleId());

                    List<String> files =  metaInfFileBO.getFiles()
                            .stream()
                            .map(Paths::get)
                            .map(Path::getFileName)
                            .map(Path::toString)
                            .collect(Collectors.toUnmodifiableList());

                    exchange.getIn().setBody(files);
                })
                .to("seda:sendImportData");

        from("seda:sendImportData")
                .process(exchange -> {
                    String fileName = (String) exchange.getIn().getHeader("CamelFileName");
                    String scanType = StringUtils.substringAfterLast(fileName, '/').replace(".json", "");
                    exchange.getIn().setHeader(RouteConstants.SCAN_STATUS, ScanStatus.IN_PROGRESS);
                    exchange.getIn().setHeader(RouteConstants.SCAN_STATUS_TYPE, ScanStatusType.PER_ROUTE);
                    exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
                })
                // Send Scan Status
//                .to("direct:scanStatusPublisher")
                .split().body()
                .streaming()
                .aggregate(body(), new UseOriginalAggregationStrategy())
                .to("direct:uploadImportData")
                .completionSize(header(Exchange.SPLIT_SIZE))
                .log("END === ${body}");

        from("direct:uploadImportData")
                .split().tokenize("\\n").streaming()
                .log("SPLIT BODY === ${body}")
                .choice()
                .when(header(Exchange.SPLIT_COMPLETE).isEqualTo(true))
                .log("SCAN ENTITY COMPLETE!")
                // SEND SCAN DATA ENTITY COMPLETE STATUS!
                .end()
                .log("DONE!");
    }
}

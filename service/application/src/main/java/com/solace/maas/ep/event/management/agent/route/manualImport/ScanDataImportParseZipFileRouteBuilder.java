package com.solace.maas.ep.event.management.agent.route.manualImport;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.model.dataformat.ZipFileDataFormat;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Iterator;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.IMPORT_ID;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanDataImportParseZipFileRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        ZipFileDataFormat zipFileDataFormat = new ZipFileDataFormat();
        zipFileDataFormat.setUsingIterator("true");

        from("direct:checkZipSizeAndUnzipFiles")
                .routeId("checkZipSizeAndUnzipFiles")
                .pollEnrich()
                .simple("file://data_collection/import/compressed_data_collection?fileName=${header." + Exchange.FILE_NAME + "}&noop=true&idempotent=false")
                .unmarshal(zipFileDataFormat)
                .split(bodyAs(Iterator.class))
                .aggregate(exchangeProperty(IMPORT_ID), new UseLatestAggregationStrategy())
                .completionPredicate(header(Exchange.SPLIT_COMPLETE).isEqualTo(true))
                .setProperty("FILE_LIST_SIZE", header(Exchange.SPLIT_SIZE))
                .to("direct:unzipImportFiles");

        from("direct:unzipImportFiles")
                .pollEnrich()
                .simple("file://${header." + Exchange.FILE_PARENT + "}?fileName=${header." + Exchange.FILE_NAME_ONLY + "}&noop=true&idempotent=false")
                .split(new ZipSplitter())
                .streaming()
                .toD("file://data_collection/import/unzipped_data_collection/${header." + IMPORT_ID + "}")
                .aggregate(exchangeProperty(IMPORT_ID), new UseLatestAggregationStrategy())
                .completionSize(exchangeProperty("FILE_LIST_SIZE"))
                .to("direct:continueParsingUnzippedFiles");
    }
}

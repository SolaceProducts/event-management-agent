package com.solace.maas.ep.event.management.agent.route.manualImport;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.route.ep.aggregation.FileZipperAggregationStrategy;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import com.solace.maas.ep.event.management.agent.scanManager.model.MetaInfFileBO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.ShareUnitOfWorkAggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.aggregate.zipfile.ZipAggregationStrategy;
import org.apache.camel.util.concurrent.SynchronousExecutorService;
import org.springframework.stereotype.Component;

@Component
public class ScanDataFilesZipperRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:writeMetaInfAndZipFiles?blockWhenFull=true&size=100")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .marshal().json(JsonLibrary.Jackson, MetaInfFileBO.class, true)
                .to("file://data_collection/?charset=utf-8&fileName=" +
                        "${header." + RouteConstants.SCHEDULE_ID +
                        "}/${header." + RouteConstants.SCAN_ID +
                        "}/${header." + RouteConstants.SCAN_TYPE + "}.json")
                .to("direct:zipScanFiles");

        from("direct:zipScanFiles")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .setBody(simple("${exchangeProperty.FILES}"))
                .split().body()
                .aggregationStrategy(new ShareUnitOfWorkAggregationStrategy(new UseLatestAggregationStrategy()))
                .setHeader("FILE_LIST_SIZE", header(Exchange.SPLIT_SIZE))
                .pollEnrich()
                .simple("file://${body.path}?fileName=${body.name}&noop=true&idempotent=false")
                .aggregationStrategy(new FileZipperAggregationStrategy())
                .setProperty(Exchange.BATCH_SIZE, header("FILE_LIST_SIZE"))
                .aggregate(header(RouteConstants.SCAN_ID), new ZipAggregationStrategy(true, true))
                .executorService(new SynchronousExecutorService())
                .completionFromBatchConsumer()
                .setHeader(Exchange.FILE_NAME, simple("${header." + RouteConstants.SCAN_ID + "}.zip"))
                .to("file://data_collection/zip?fileExist=override");

        from("direct:downloadZipFile")
                .pollEnrich()
                .simple("file://data_collection/zip?fileName=${header." +
                        RouteConstants.SCAN_ID + "}.zip&noop=true&idempotent=false");
    }
}

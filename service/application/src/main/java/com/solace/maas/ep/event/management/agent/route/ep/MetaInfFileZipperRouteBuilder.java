package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.route.ep.aggregation.FileParseAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.ShareUnitOfWorkAggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.aggregate.zipfile.ZipAggregationStrategy;
import org.apache.camel.util.concurrent.SynchronousExecutorService;
import org.springframework.stereotype.Component;

@Component
public class MetaInfFileZipperRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:metaInfCollectionFileWrite") //?blockWhenFull=true&size=" + Integer.MAX_VALUE)
                .unmarshal().json(true)
                .marshal().json(true)
                .to("file://data_collection/?fileExist=append&charset=utf-8&fileName=" +
                        "${header." + RouteConstants.SCHEDULE_ID +
                        "}/${header." + RouteConstants.SCAN_ID +
                        "}/${header." + RouteConstants.SCAN_TYPE + "}.json")
                .to("direct:zipFiles");

        from("direct:zipFiles") //?blockWhenFull=true")
                .setBody(simple("${exchangeProperty.FILES}"))
                .split().body()
                .aggregationStrategy(new ShareUnitOfWorkAggregationStrategy(new UseLatestAggregationStrategy()))
                .setHeader("FILE_LIST_SIZE", header(Exchange.SPLIT_SIZE))
                .pollEnrich()
                .simple("file://${body.path}?fileName=${body.name}&noop=true&idempotent=false")
                .aggregationStrategy(new FileParseAggregationStrategy())
                .setProperty(Exchange.BATCH_SIZE, header("FILE_LIST_SIZE"))
                .aggregate(header(RouteConstants.SCAN_ID), new ZipAggregationStrategy(true, true))
                .executorService(new SynchronousExecutorService())
                .completionFromBatchConsumer()
                .setHeader(Exchange.FILE_NAME, simple("${header." + RouteConstants.SCAN_ID + "}.zip"))
                .to("file://data_collection/zip?fileExist=override");

        from("direct:downloadZip")
                .pollEnrich()
                .simple("file://data_collection/zip?fileName=${header." + RouteConstants.SCAN_ID + "}.zip&noop=true&idempotent=false")
                .log("getting file");
    }
}

package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.route.ep.aggregation.FileParseAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.zipfile.ZipAggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class MetaInfFileZipperRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:metaInfCollectionFileWrite?blockWhenFull=true&size=" + Integer.MAX_VALUE)
                .unmarshal().json(true)
                .marshal().json(true)
                .to("file://data_collection/?fileExist=append&charset=utf-8&fileName=" +
                        "${header." + RouteConstants.SCHEDULE_ID +
                        "}/${header." + RouteConstants.SCAN_ID +
                        "}/${header." + RouteConstants.SCAN_TYPE + "}.json")
                .to("seda:zipFiles");


        from("seda:zipFiles?blockWhenFull=true")
                .setBody(simple("${exchangeProperty.FILES}"))
                .split().body().streaming()
                .pollEnrich()
                .simple("file://${body.path}?fileName=${body.name}&noop=true&idempotent=false")
                .aggregationStrategy(new FileParseAggregationStrategy())
                .aggregate(constant(true), new ZipAggregationStrategy())
                .completionFromBatchConsumer()
                .eagerCheckCompletion()
                .to("file://data_collection/zip");
    }
}

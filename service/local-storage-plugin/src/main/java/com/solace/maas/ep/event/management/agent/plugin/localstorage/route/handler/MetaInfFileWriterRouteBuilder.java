package com.solace.maas.ep.event.management.agent.plugin.localstorage.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.zipfile.ZipAggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class MetaInfFileWriterRouteBuilder extends RouteBuilder {

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

    public static class FileParseAggregationStrategy implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            newExchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID,
                    oldExchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID));
            newExchange.getIn().setHeader(RouteConstants.SCAN_ID,
                    oldExchange.getIn().getHeader(RouteConstants.SCAN_ID));
            newExchange.getIn().setHeader(RouteConstants.SCHEDULE_ID,
                    oldExchange.getIn().getHeader(RouteConstants.SCHEDULE_ID));
            newExchange.getIn().setHeader(RouteConstants.SCAN_TYPE,
                    oldExchange.getIn().getHeader(RouteConstants.SCAN_TYPE));

            return newExchange;
        }
    }
}

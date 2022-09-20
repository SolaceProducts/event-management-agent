package com.solace.maas.ep.runtime.agent.plugin.localstorage.route.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.runtime.agent.plugin.constants.AggregationConstants;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.localstorage.processor.output.file.AggregationFileWriteProcessor;
import com.solace.maas.ep.runtime.agent.plugin.localstorage.route.aggregation.FileDataMergeAggregationStrategyImpl;
import com.solace.maas.ep.runtime.agent.plugin.localstorage.route.aggregation.FileParseAggregationStrategyImpl;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ExcludeFromJacocoGeneratedReport
@Component
public class FileAggregationWriterRouteBuilder extends RouteBuilder {
    private final AggregationFileWriteProcessor aggregationFileWriteProcessor;

    private final ObjectMapper objectMapper;

    @Autowired
    public FileAggregationWriterRouteBuilder(AggregationFileWriteProcessor aggregationFileWriteProcessor) {
        super();
        this.aggregationFileWriteProcessor = aggregationFileWriteProcessor;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void configure() throws Exception {
        from("seda:fileReader?blockWhenFull=true")
                .streamCaching()
                .setHeader(AggregationConstants.COMPLETION_COUNT, constant(0))
                .setBody(simple("${exchangeProperty.FILES}"))
                .split().body().streaming()
                .setProperty(AggregationConstants.FILE_SPLIT_COMPLETE, exchangeProperty("Exchange.SPLIT_COMPLETE"))
                .setHeader(AggregationConstants.OBJECT_KEY, simple("${body.key}"))
                .pollEnrich()
                .simple("file://${body.path}?fileName=${body.name}&noop=true&idempotent=false")
                .aggregationStrategy(new FileParseAggregationStrategyImpl())
                .timeout(60_000)
                .to("seda:fileAggregation");

        from("seda:fileAggregation?blockWhenFull=true&size=" + Integer.MAX_VALUE)
                .streamCaching()
                .split().tokenize("\\n").streaming()
                .aggregate(header(RouteConstants.AGGREGATION_ID).append("_").append(header(Exchange.FILE_NAME)),
                        new FileDataMergeAggregationStrategyImpl(objectMapper))
                .completionPredicate(simple("${exchangeProperty." + Exchange.SPLIT_COMPLETE + "} == true"))
                .completionTimeout(60_000)
                .marshal().json(JsonLibrary.Jackson, true)
                .choice().when(simple("${exchangeProperty." + AggregationConstants.FILE_SPLIT_COMPLETE +
                        "} != true"))
                .transform(body().append(", "))
                .end()
                .toD("file://data_collection/aggregation/?fileExist=append&fileName=${header." + RouteConstants.AGGREGATION_ID + "}.json")
                .setHeader(AggregationConstants.AGGREGATED_FILE_PATH,
                        simple("data_collection/aggregation/${header." + RouteConstants.AGGREGATION_ID + "}.json"))
                .setHeader("FILES", simple("${exchangeProperty.FILES}"))
                .to("seda:fileAggregationDBWriter");

        from("seda:fileAggregationDBWriter?blockWhenFull=true&size=" + Integer.MAX_VALUE)
                .process(aggregationFileWriteProcessor);
    }
}

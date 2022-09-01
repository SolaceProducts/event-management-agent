package com.solace.maas.ep.runtime.agent.plugin.route.handler.base;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.Objects;

import static org.apache.camel.support.builder.PredicateBuilder.or;

@ExcludeFromJacocoGeneratedReport
public class DataAggregationRouteBuilder extends DataPublisherRouteBuilder {
    protected final AggregationStrategy aggregationStrategy;

    protected final Integer aggregationSize;

    /**
     * @param processor           The Processor handling the Data Collection for a Scan.
     * @param routeId             The Unique Identifier for this Route.
     * @param routeType
     * @param routeManager        The list of Route Destinations the Data Collection events will be streamed to.
     * @param aggregationStrategy
     * @param aggregationSize
     */
    public DataAggregationRouteBuilder(Processor processor, String routeId, String routeType, RouteManager routeManager,
                                       AggregationStrategy aggregationStrategy, Integer aggregationSize) {
        super(processor, routeId, routeType, routeManager);

        this.aggregationStrategy = aggregationStrategy;
        this.aggregationSize = aggregationSize;
    }

    /**
     * Configuring main Camel Route for Messaging Service scans.
     */
    @SuppressWarnings("CPD-START")
    @Override
    public void configure() {
        from("seda:" + routeId + "?blockWhenFull=true&size=1000000")
                // Define a Route ID so we can kill this Route if needed.
                .routeId(routeId)
                .setHeader(RouteConstants.SCAN_TYPE, constant(routeType))
                .setHeader("RECIPIENTS", method(this, "getRecipients(${header."
                        + RouteConstants.SCAN_ID + "})"))
                .setHeader("DESTINATIONS", method(this, "getDestinations(${header."
                        + RouteConstants.SCAN_ID + "})"))
                // Data Collected by the Processor is expected to be an Array. We'll be splitting this Array
                // and streaming it back to interested parties. Interceptors / Destination routes will need to
                // aggregate this data together if they need it all at once.
                .split(body()).streaming().parallelProcessing()
                .choice().when(header(Exchange.SPLIT_COMPLETE).isEqualTo(true))
                .setHeader("DATA_PROCESSING_COMPLETE", constant(true))
                .endChoice()
                .end()

                .aggregate(header(RouteConstants.SCAN_ID))
                .aggregationStrategy(aggregationStrategy)
                .completionSize(aggregationSize)
                .completionPredicate(simple("${header.DATA_PROCESSING_COMPLETE} == true"))
                // Injecting the Data Collection Processor. This will normally be the processor that
                // connects to the Messaging Service.
                .log("agg complete ${body}")
                .process(processor)
                // Data Collected by the Processor is expected to be an Array. We'll be splitting this Array
                // and streaming it back to interested parties. Interceptors / Destination routes will need to
                // aggregate this data together if they need it all at once.
                .split(body()).streaming().parallelProcessing()

                // The last aggregation bundle will have "DATA_PROCESSING_COMPLETE" set true in the header for all
                // messages in the aggregation bundle.
                // We want to set the "DATA_PROCESSING_COMPLETE" to false, except for the last message in the
                // aggregation bundle.
                .choice().when(or(header(Exchange.SPLIT_COMPLETE).isEqualTo(false),
                        header("DATA_PROCESSING_COMPLETE").isEqualTo(false)))
                .setHeader("DATA_PROCESSING_COMPLETE", constant(false))
                .endChoice()
                .end()

                // The Route Interceptors are injected here. They are called Asynchronously and don't return a response
                // to this Route.
                .recipientList().header("RECIPIENTS").delimiter(";")
                .parallelProcessing()
                // Transforming the Events to JSON. Do we need to do this here? Maybe we should delegate this to the
                // destinations instead?
                .marshal().json(JsonLibrary.Jackson)
                // The Destinations receiving the Data Collection events get called here.
                .recipientList().header("DESTINATIONS").delimiter(";");

        if (Objects.nonNull(routeManager)) {
            routeManager.setupRoute(routeId);
        }
    }
}

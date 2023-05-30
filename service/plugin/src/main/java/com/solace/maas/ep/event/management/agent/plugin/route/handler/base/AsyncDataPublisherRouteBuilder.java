package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.SchedulerConstants;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
public class AsyncDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    private final AsyncRoutePublisherImpl asyncRoutePublisher;

    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     * @param routeId      The Unique Identifier for this Route.
     * @param routeType
     * @param routeManager The list of Route Destinations the Data Collection events will be streamed to.
     * @param mdcProcessor The Processor handling the MDC data for logging.
     */
    public AsyncDataPublisherRouteBuilder(Processor processor, AsyncRoutePublisherImpl asyncRoutePublisher, String routeId,
                                          String routeType, RouteManager routeManager, MDCProcessor mdcProcessor,
                                          EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, routeId, routeType, routeManager, mdcProcessor, emptyScanEntityProcessor);

        this.asyncRoutePublisher = asyncRoutePublisher;
    }

    @Override
    public void configure() {
        interceptFrom()
                .setHeader(RouteConstants.SCAN_TYPE, constant(routeType))
                .process(mdcProcessor);

        CamelReactiveStreamsService camel = CamelReactiveStreams.get(getContext());

        from("seda:" + routeId + "?blockWhenFull=true&size=1000000")
                .routeId(routeId)
                .log("EXECUTION TIMER: ${header" + SchedulerConstants.SCHEDULER_TERMINATION_TIMER + "}")
                .choice()
                .when(simple("${header." + SchedulerConstants.SCHEDULER_TERMINATION_TIMER + "} == true"))
                .setBody(exchange -> {
                    String scanId = exchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class);
                    String scanType = exchange.getIn().getHeader(RouteConstants.SCAN_TYPE, String.class);

                    Map<String, Object> signal = new HashMap<>();
                    signal.put(RouteConstants.SCAN_ID, scanId);
                    signal.put(RouteConstants.SCAN_TYPE, scanType);

                    return signal;
                })
                .to("seda://jobScheduler")
                .end()
                .to("reactive-streams:asyncProcessing_" + routeId);

        from("seda:asyncEvent_" + routeId + "?blockWhenFull=true&size=1000000")
                .setHeader(RouteConstants.SCAN_TYPE, constant(routeType))
                .setHeader("RECIPIENTS", method(this, "getRecipients(${header."
                        + RouteConstants.SCAN_ID + "})"))
                .setHeader("DESTINATIONS", method(this, "getDestinations(${header."
                        + RouteConstants.SCAN_ID + "})"))
                .process(processor)
                .recipientList().header("RECIPIENTS").delimiter(";")
                .shareUnitOfWork()
                .split(body()).streaming().shareUnitOfWork()
                .marshal().json(JsonLibrary.Jackson)
                .recipientList().header("DESTINATIONS").delimiter(";")
                .shareUnitOfWork();

        Publisher<Exchange> exchanges = camel.fromStream("asyncProcessing_" + routeId);

        Flux.from(exchanges)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(asyncRoutePublisher::start);

        if (Objects.nonNull(routeManager)) {
            routeManager.setupRoute(routeId);
        }

        try {
            camel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

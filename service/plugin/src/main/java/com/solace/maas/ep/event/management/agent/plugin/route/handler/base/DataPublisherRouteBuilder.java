package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DataPublisherRouteBuilder creates Camel Routes intended to handle Messaging Service scans.
 */
@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
public class DataPublisherRouteBuilder extends AbstractRouteBuilder {
    protected final Processor processor;

    protected final String routeId;

    protected final String routeType;

    protected final RouteManager routeManager;

    protected final MDCProcessor mdcProcessor;

    protected final ScanTypeDescendentsProcessor scanTypeDescendentsProcessor;

    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     * @param routeId      The Unique Identifier for this Route.
     * @param routeManager The list of Route Destinations the Data Collection events will be streamed to.
     * @param mdcProcessor The Processor handling the MDC data for logging.
     */
    public DataPublisherRouteBuilder(Processor processor, String routeId,
                                     String routeType, RouteManager routeManager,
                                     MDCProcessor mdcProcessor,
                                     ScanTypeDescendentsProcessor scanTypeDescendentsProcessor) {
        super(scanTypeDescendentsProcessor);

        this.processor = processor;
        this.routeId = routeId;
        this.routeType = routeType;
        this.routeManager = routeManager;
        this.mdcProcessor = mdcProcessor;
        this.scanTypeDescendentsProcessor = scanTypeDescendentsProcessor;
    }

    /**
     * Configuring main Camel Route for Messaging Service scans.
     */
    @SuppressWarnings("CPD-START")
    @Override
    public void configure() throws Exception {

        super.configure();

        interceptFrom()
                .setHeader(RouteConstants.SCAN_TYPE, constant(routeType))
                .process(mdcProcessor);

        from("seda:" + routeId + "?blockWhenFull=true&size=100")
                // Define a Route ID so we can kill this Route if needed.
                .process(exchange -> MDC.put("scanId", exchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class)))
                .routeId(routeId)
                .setHeader(RouteConstants.SCAN_TYPE, constant(routeType))
                .setHeader("RECIPIENTS", method(this, "getRecipients(${header."
                        + RouteConstants.SCAN_ID + "})"))
                .setHeader("DESTINATIONS", method(this, "getDestinations(${header."
                        + RouteConstants.SCAN_ID + "})"))
                .setHeader(RouteConstants.SCAN_STATUS, constant(ScanStatus.IN_PROGRESS))
                .to("direct:markRouteScanStatusInProgress?block=false&failIfNoConsumers=false")

                // Injecting the Data Collection Processor. This will normally be the processor that
                // connects to the Messaging Service.
                .process(processor)
                // Checking for empty scan types. In case an empty scan type is encountered, the recipients are not injected. Rather,
                // they are retrieved from the empty scan recipients store and a complete status message is sent to each recipient.
                .choice().when(simple("${body.size} == 0"))
                .setHeader(RouteConstants.IS_EMPTY_SCAN_TYPES, constant(true))
                .process(scanTypeDescendentsProcessor)
                .split(simple("${header." + RouteConstants.SCAN_TYPE + "}"))
                .to("direct:markRouteScanStatusComplete?block=false&failIfNoConsumers=false")
                .end()
                .endChoice()
                .otherwise()
                // The Route Interceptors are injected here. They are called Asynchronously and don't return a response
                // to this Route.
                .recipientList().header("RECIPIENTS").delimiter(";")
                .split(body()).streaming().shareUnitOfWork()
                // Transforming the Events to JSON. Do we need to do this here? Maybe we should delegate this to the
                // destinations instead?
                .marshal().json(JsonLibrary.Jackson)
                .choice().when(header(Exchange.SPLIT_COMPLETE).isEqualTo(true))
                .setHeader("DATA_PROCESSING_COMPLETE", constant(true))
                .endChoice()
                .end()
                // The Destinations receiving the Data Collection events get called here.
                .recipientList().header("DESTINATIONS").delimiter(";")
                .choice().when(header("DATA_PROCESSING_COMPLETE").isEqualTo(true))
                .to("direct:markRouteScanStatusComplete?block=false&failIfNoConsumers=false")
                .endChoice()
                .end()
                .endChoice()
                .end();

        if (Objects.nonNull(routeManager)) {
            routeManager.setupRoute(routeId);
        }
    }

    public String getRecipients(String scanId) {
        List<String> recipients = new ArrayList<>();

        if (Objects.nonNull(routeManager)) {
            recipients.addAll(routeManager.getRecipientList(scanId, routeId));
        }

        return String.join(";", recipients);
    }

    public String getDestinations(String scanId) {
        List<String> destinations = new ArrayList<>();

        if (Objects.nonNull(routeManager)) {
            destinations.addAll(routeManager.getDestinationList(scanId, routeId));
        }

        return String.join(";", destinations);
    }

    protected void exceptionHandling() throws Exception {
        super.configure();
    }
}

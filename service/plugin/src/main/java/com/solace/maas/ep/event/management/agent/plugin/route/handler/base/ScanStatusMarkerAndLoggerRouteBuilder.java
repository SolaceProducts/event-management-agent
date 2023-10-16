package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ScanStatusMarkerAndLoggerRouteBuilder extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:markRouteScanStatusInProgress")
                .routeId("markRouteScanStatusInProgress")
                .log("Scan request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: actorId [${header." + RouteConstants.ACTOR_ID + "}]:" +
                        " The status of [${header." +
                        RouteConstants.SCAN_TYPE + "}]" + " is: [" + ScanStatus.IN_PROGRESS + "].")
                .to("direct:perRouteScanStatusPublisher?block=false&failIfNoConsumers=false")
                .log("Scan request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: actorId [${header." + RouteConstants.ACTOR_ID + "}]:" +
                        " Retrieving [${header." + RouteConstants.SCAN_TYPE
                        + "}] details from event broker [${header." + RouteConstants.MESSAGING_SERVICE_ID + "}].");


        from("direct:markRouteScanStatusComplete")
                .routeId("markRouteScanStatusComplete")
                .to("direct:processScanStatusAsComplete?block=false&failIfNoConsumers=false")
                .log("Scan request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: actorId [${header." + RouteConstants.ACTOR_ID + "}]:" +
                        "The status of [${header." +
                        RouteConstants.SCAN_TYPE + "}]" + " is: [" + ScanStatus.COMPLETE + "].");


        from("direct:markRouteImportStatusInProgress")
                .routeId("markRouteImportStatusInProgress")
                .log("Scan import request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: actorId [${header." + RouteConstants.ACTOR_ID + "}]:" +
                        "The status of [${header." +
                        RouteConstants.SCAN_TYPE + "}]" + " is: [" + ScanStatus.IN_PROGRESS + "].")
                .to("direct:perRouteScanStatusPublisher?block=false&failIfNoConsumers=false")
                .log("Scan import request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: actorId [${header." + RouteConstants.ACTOR_ID + "}]:" +
                        "The status of [${header." +
                        RouteConstants.SCAN_TYPE + "}] details from file: [${body.fileName}].");


        from("direct:markRouteImportStatusComplete")
                .routeId("markRouteImportStatusComplete")
                .to("direct:processScanStatusAsComplete?block=false&failIfNoConsumers=false")
                .log("Scan import request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: actorId [${header." + RouteConstants.ACTOR_ID + "}]:" +
                        "The status of [${header." +
                        RouteConstants.SCAN_TYPE + "}]" + " is: [" + ScanStatus.COMPLETE + "].");
    }
}

package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportFileProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanLogsImportLogEventsProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanLogsImportProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import static org.apache.camel.support.builder.PredicateBuilder.and;
import static org.apache.camel.support.builder.PredicateBuilder.not;
import static org.apache.camel.support.builder.PredicateBuilder.or;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportRouteBuilder extends RouteBuilder {

    private final ScanDataImportFileProcessor scanDataImportFileProcessor;
    private final ScanLogsImportProcessor scanLogsImportProcessor;
    private final ScanLogsImportLogEventsProcessor scanLogsImportLogEventsProcessor;

    Predicate filesFilter = and(
            or(
                    header("CamelFileName").endsWith(".json"),
                    header("CamelFileName").endsWith(".log")
            ),
            and(
                    not(header("CamelFileName").contains("general-logs")),
                    not(header("CamelFileName").contains("META_INF"))
            ));

    @Autowired
    public ScanDataImportRouteBuilder(ScanDataImportFileProcessor scanDataImportFileProcessor,
                                      ScanLogsImportProcessor scanLogsImportProcessor,
                                      ScanLogsImportLogEventsProcessor scanLogsImportLogEventsProcessor) {
        super();
        this.scanDataImportFileProcessor = scanDataImportFileProcessor;
        this.scanLogsImportLogEventsProcessor = scanLogsImportLogEventsProcessor;
        this.scanLogsImportProcessor = scanLogsImportProcessor;
    }

    @Override
    public void configure() {
        from("seda:manualImport?blockWhenFull=true&size=100")
                .routeId("manualImport")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .split(new ZipSplitter())
                .streaming()
                .filter(filesFilter)
                // send scan status message here.
                .split().tokenize("\\n").streaming()
//                .convertBodyTo(String.class)
                .to("seda:processImportFiles");


        from("seda:processImportFiles")
                .routeId("processImportFiles")
                .setHeader(RouteConstants.IS_DATA_IMPORT, constant(true))
                .choice()

                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header("CamelFileName").contains(header(RouteConstants.SCAN_ID))))
                .to("seda:streamDataAndProcessEndOfImportStatus")
                .endChoice()

                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header(RouteConstants.SCAN_ID).isNull()))
                .to("seda:streamDataAndProcessEndOfImportStatus")
                .endChoice()

                .when(and(header(RouteConstants.SCHEDULE_ID).isNull(), header(RouteConstants.SCAN_ID).isNull()))
                .to("seda:processScanDataAndLogs")
                .endChoice()

                .end();

        from("seda:streamDataAndProcessEndOfImportStatus")
                .routeId("streamDataAndProcessEndOfImportStatus")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .process(scanDataImportFileProcessor)
//                .to("seda:scanStatusPublisher")
                .to("seda:eventPortal")
                //                .header(Exchange.SPLIT_COMPLETE)
                .to("seda:processEndOfImportStatus");


        from("seda:processEndOfImportStatus")
                .routeId("processEndOfImportStatus")
                .setHeader("IMPORT_PROCESSING_COMPLETE", constant(true))
                .to("seda:processScanStatus");


        from("seda:processScanDataAndLogs")
                .routeId("processScanDataAndLogs")
                .choice()
                .when(header("CamelFileName").contains(constant(".json")))
                .to("seda:streamDataAndProcessEndOfImportStatus")
                .endChoice()

                .when(header("CamelFileName").contains(constant(".log")))
                .process(scanLogsImportProcessor)
                .to("seda:scanStatusPublisher")

                .split().tokenize("\\n").streaming()
                .process(scanLogsImportLogEventsProcessor)
                .to("seda:scanLogsPublisher")
                .end()

                .to("seda:processEndOfImportStatus")
                .endChoice()
                .end();
    }
}

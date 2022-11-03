package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportFileProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanLogsImportLogEventsProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanLogsImportProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.camel.support.builder.PredicateBuilder.and;
import static org.apache.camel.support.builder.PredicateBuilder.not;
import static org.apache.camel.support.builder.PredicateBuilder.or;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportRouteBuilder extends RouteBuilder {
    private static final List<String> fileList = new ArrayList<>();

    private final ScanDataImportFileProcessor scanDataImportFileProcessor;
    private final ScanLogsImportProcessor scanLogsImportProcessor;
    private final ScanLogsImportLogEventsProcessor scanLogsImportLogEventsProcessor;

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
                .filter(or(header("CamelFileName").endsWith(".json"),
                        and(header("CamelFileName").endsWith(".log"),
                                not(header("CamelFileName").contains("general-log")))))
                .convertBodyTo(String.class)
                .to("seda:processImportFiles");


        from("seda:processImportFiles")
                .routeId("processImportFiles")
                .setHeader(RouteConstants.IS_IMPORTED_DATA, constant(true))

                .choice()

                // Covers the scenario where schedule id scan id and scan are specified.
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header("CamelFileName").contains(header(RouteConstants.SCAN_ID))))

                .aggregate(header("CamelFileName"), new FileListAggregationStrategy())
                .completionTimeout(1000)
                .process(scanDataImportFileProcessor)
                .log("Scan request: [${header." + RouteConstants.SCAN_ID + "}]: " +
                        "Importing [${header." + RouteConstants.SCAN_TYPE + "}] " +
                        "for schedule Id: [${header." + RouteConstants.SCHEDULE_ID + "}]")

                .to("seda:scanStatusPublisher")
                .split().tokenize("\\n").streaming()
                .to("seda:eventPortal")
                .end()
                .to("seda:processEndOfImportStatus")
                .endChoice()

                // Covers the scenario where only schedule is specified. All scanId folders in the schedule directory will be processed.
                .when(and(header("CamelFileName").contains(header(RouteConstants.SCHEDULE_ID)),
                        header(RouteConstants.SCAN_ID).isNull()))

                .aggregate(header("CamelFileName"), new FileListAggregationStrategy())
                .completionTimeout(1000)
                .process(scanDataImportFileProcessor)
                .log("SECOND OPTION Scan request: [${header." + RouteConstants.SCAN_ID + "}]: Importing [${header." +
                        RouteConstants.SCAN_TYPE + "}] for schedule Id: [${header." + RouteConstants.SCHEDULE_ID + "}]")

                .to("seda:scanStatusPublisher")
                .split().tokenize("\\n").streaming()
                .to("seda:eventPortal")
                .end()
                .to("seda:processEndOfImportStatus")

                .endChoice()

                // Nothing is specified. Imports all scan data and log data
                .when(and(header(RouteConstants.SCHEDULE_ID).isNull(), header(RouteConstants.SCAN_ID).isNull()))

                .aggregate(header("CamelFileName"), new FileListAggregationStrategy())
                .completionTimeout(1000)

                .choice()
                .when(header("CamelFileName").contains(constant(".json")))
                .process(scanDataImportFileProcessor)
                .to("seda:scanStatusPublisher")

                .split().tokenize("\\n").streaming()
                .to("seda:eventPortal")
                .end()
                .to("seda:processEndOfImportStatus")
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

                .end()

                .log("Scan request: [${header." + RouteConstants.SCAN_ID + "}]: Importing [${header." +
                        RouteConstants.SCAN_TYPE + "}] for schedule Id: [${header." + RouteConstants.SCHEDULE_ID + "}]")
                .endChoice()
                .end();


        from("seda:processEndOfImportStatus?blockWhenFull=true&size=100")
                .routeId("processEndOfImportStatus")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .setHeader("IMPORT_PROCESSING_COMPLETE", constant(true))
                .to("seda:processScanStatus");
    }

    public static class FileListAggregationStrategy implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            String filename = (String) newExchange.getIn().getHeader("CamelFileName");
            fileList.add(filename);

            return newExchange;
        }
    }

    public static class FileParseAggregationStrategy implements AggregationStrategy {
        private List<String> files = new ArrayList<>();

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
